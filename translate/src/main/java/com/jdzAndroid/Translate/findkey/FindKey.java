package com.jdzAndroid.Translate.findkey;

import com.jdzAndroid.Translate.bean.Pair;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Find key by value
 */
public class FindKey {
    private final FindKeyConfig mConfig;

    public FindKey(FindKeyConfig config) {
        mConfig = config;
    }

    public void findKey() {
        try {
            if (mConfig == null) {
                System.out.println("Please configure the basic findKey config information in the gradle file of the module.");
                return;
            }
            if (mConfig.mStartLine < 0 || mConfig.mEndLine < mConfig.mStartLine) {
                System.out.println("startLine or endLine invalid");
                return;
            }
            if (mConfig.mExcelFilePath == null || mConfig.mExcelFilePath.length() == 0) {
                System.out.println("Please configure the findKey config source file path");
                return;
            }
            if (mConfig.mCompareFilePath == null || mConfig.mCompareFilePath.length() == 0) {
                System.out.println("Please configure the findKey config compare file path");
                return;
            }
            if (mConfig.mOutPath == null || mConfig.mOutPath.length() == 0) {
                System.out.println("Please configure the findKey config out file path");
                return;
            }
            if (mConfig.mCompareIndex < 0) {
                System.out.println("compareIndex is invalid");
                return;
            }
            if (mConfig.mExcelSheetIndex < 0) {
                System.out.println("excelSheetIndex is invalid");
                return;
            }
            List<String> unFindList = new ArrayList<>();
            File outputFile = new File(mConfig.mOutPath);
            if (!outputFile.exists() || !outputFile.isDirectory()) outputFile.mkdirs();
            File file = new File(mConfig.mExcelFilePath);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(Math.max(0, mConfig.mExcelSheetIndex));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document compareDocument = documentBuilder.parse(mConfig.mCompareFilePath);
            NodeList compareRootNode = compareDocument.getElementsByTagName("string");
            int compareRootNodeSize = compareRootNode.getLength();
            int ignoreValueSize = mConfig.mIgnoreValueList.size();
            List<Pair> compareList = new ArrayList<>();
            System.out.println("compareRootNodeSize=" + compareRootNodeSize);
            if (compareRootNodeSize > 0) {
                for (int i = 0; i < compareRootNodeSize; i++) {
                    Node item = compareRootNode.item(i);
                    String itemContent = item.getTextContent();
                    if (itemContent != null) {
                        itemContent = itemContent.replaceAll(" ", "");
                        for (int j = 0; j < ignoreValueSize; j++) {
                            String ignoreValue = mConfig.mIgnoreValueList.get(j);
                            itemContent = itemContent.replaceAll(ignoreValue, "");
                        }
                    }
                    compareList.add(new Pair(item.getAttributes().getNamedItem("name").getNodeValue(), itemContent,
                            item.getTextContent()));
                }
            }
            int compareListSize = compareList.size();
            Document outPutDocument = documentBuilder.newDocument();
            outPutDocument.setXmlStandalone(true);
            Element outputElement = outPutDocument.createElement("resources");
            for (int i = 0; i <= Integer.MAX_VALUE - 1; i++) {
                if (i < mConfig.mStartLine) continue;
                if (i>mConfig.mEndLine)break;
                try {
                    XSSFRow row = xssfSheet.getRow(i);
                    if (row == null) {
                        System.out.println("row=null");
                        continue;
                    }
                    XSSFCell firstCell = row.getCell(mConfig.mCompareIndex);
                    if (firstCell == null) continue;
                    String copySourceContent = firstCell.toString();
                    if (mConfig.mIgnoreValueList.size() > 0) {
                        for (String ignoreValue : mConfig.mIgnoreValueList) {
                            copySourceContent = copySourceContent.replaceAll(ignoreValue, "");
                        }
                    }
                    System.out.println("copySourceContent=" + copySourceContent);
                    String key = null;
                    for (int j = 0; j < compareListSize; j++) {
                        Pair itemPair = compareList.get(j);
                        if (itemPair == null) continue;
                        if (copySourceContent.contentEquals(itemPair.value)) {
                            key = itemPair.key;
                            break;
                        }
                    }
                    System.out.println("key=" + key);
                    if (key == null || key.length() == 0) {
                        unFindList.add(firstCell.toString());
                        continue;
                    }
                    XSSFCell cell = row.getCell(mConfig.mCompareIndex);
                    if (cell != null) {
                        String sourceContent = cell.toString();
                        Element itemElement = outPutDocument.createElement("string");
                        itemElement.setAttribute("name", key);
                        itemElement.setTextContent(sourceContent);
                        outputElement.appendChild(itemElement);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            outPutDocument.appendChild(outputElement);
            transformer.transform(new DOMSource(outPutDocument), new StreamResult(new File(mConfig.mOutPath + "\\findKey.xml")));
            //The output is a translation that failed to match
            Document failedDocument = documentBuilder.newDocument();
            failedDocument.setXmlStandalone(true);
            Element failedRootElement = failedDocument.createElement("resources");
            int size = unFindList.size();
            for (int index = 0; index < size; index++) {
                String value = unFindList.get(index);
                if (value == null || value.length() == 0) continue;
                Element itemElement = failedDocument.createElement("string");
                itemElement.setAttribute("name", String.valueOf(index));
                itemElement.setTextContent(value);
                failedRootElement.appendChild(itemElement);
            }
            failedDocument.appendChild(failedRootElement);
            transformer.transform(new DOMSource(failedDocument), new StreamResult(new File(mConfig.mOutPath + "\\unfind.xml")));
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}