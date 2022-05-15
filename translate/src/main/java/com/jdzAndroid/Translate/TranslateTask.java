package com.jdzAndroid.Translate;

import com.jdzAndroid.base.BaseTask;

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

public class TranslateTask implements BaseTask {

    private TranslateConfig mTranslateConfig;

    public TranslateTask(TranslateConfig translateConfig) {
        mTranslateConfig = translateConfig;
    }

    @Override
    public void startWork() {
        translate();
    }

    private boolean validBasicInfo() {
        if (mTranslateConfig == null) {
            printLog("Please configure the basic translation information in the gradle file of the module.");
            return false;
        }
        if (mTranslateConfig.mExcelFilePath == null || mTranslateConfig.mExcelFilePath.length() == 0) {
            printLog("Please configure the translation source file path");
            return false;
        }
        if (mTranslateConfig.mLanguageCodeList.size() != mTranslateConfig.mColumnList.size()) {
            printLog("The size of the country language code must be equal to the country language column number code");
            return false;
        }
        int replacedValueListSize = mTranslateConfig.mReplacedValueList.size();
        if (replacedValueListSize != mTranslateConfig.mNewValueList.size()) {
            printLog("The size of the text list to be replaced and the new value list after replacement must be the same");
            return false;
        }
        return true;
    }

    public void translate() {
        try {
            if (!validBasicInfo()) return;
            File outputFile = new File(mTranslateConfig.mOutPath);
            if (!outputFile.exists()) outputFile.mkdirs();
            File file = new File(mTranslateConfig.mExcelFilePath);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(Math.max(0, mTranslateConfig.mExcelSheetIndex));
            int firstRowNum = Math.max(xssfSheet.getFirstRowNum(), mTranslateConfig.mStartLine);
            int lastRowNum = xssfSheet.getLastRowNum();
            if (mTranslateConfig.mEndLine >= firstRowNum && mTranslateConfig.mEndLine <= lastRowNum)
                lastRowNum = mTranslateConfig.mEndLine;

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            List<Document> documentList = new ArrayList<>();
            List<Element> elementList = new ArrayList<>();
            Document compareDocument = documentBuilder.parse(mTranslateConfig.mCompareFilePath);
            NodeList compareRootNode = compareDocument.getElementsByTagName("string");
            int compareRootNodeSize = compareRootNode.getLength();
            int ignoreValueSize = mTranslateConfig.mIgnoreValueList.size();
            List<Pair> compareList = new ArrayList<>();
            if (compareRootNodeSize > 0) {
                for (int i = 0; i < compareRootNodeSize; i++) {
                    Node item = compareRootNode.item(i);
                    String itemContent = item.getTextContent();
                    if (itemContent != null) {
                        for (int j = 0; j < ignoreValueSize; j++) {
                            String ignoreValue = mTranslateConfig.mIgnoreValueList.get(j);
                            itemContent = itemContent.replaceAll(ignoreValue, "");
                        }
                    }
                    compareList.add(new Pair(item.getAttributes().getNamedItem("name").getNodeValue(), itemContent,
                            item.getTextContent()));
                }
            }
            int languageCodeSize = mTranslateConfig.mLanguageCodeList.size();
            for (int i = 0; i < languageCodeSize; i++) {
                Document document = documentBuilder.newDocument();
                document.setXmlStandalone(true);
                elementList.add(document.createElement("resources"));
                documentList.add(document);
            }
            int compareListSize = compareList.size();

            for (int i = firstRowNum; i <= lastRowNum; i++) {
                XSSFRow row = xssfSheet.getRow(i);
                if (row == null) continue;
                XSSFCell firstCell = row.getCell(mTranslateConfig.mCompareIndex);
                if (firstCell == null) continue;
                String copySourceContent = firstCell.toString().replaceAll(" ", "");
                if (mTranslateConfig.mIgnoreValueList.size() > 0) {
                    for (String ignoreValue : mTranslateConfig.mIgnoreValueList) {
                        copySourceContent = copySourceContent.replaceAll(ignoreValue, "");
                    }
                }
                String key = null;
                for (int j = 0; j < compareListSize; j++) {
                    Pair itemPair = compareList.get(j);
                    if (itemPair == null) continue;
                    if (copySourceContent.contentEquals(itemPair.value)) {
                        key = itemPair.key;
                        compareList.set(j, null);
                        break;
                    }
                }
                if (key == null || key.length() == 0) continue;
                for (int j = 0; j < languageCodeSize; j++) {
                    XSSFCell cell = row.getCell(mTranslateConfig.mColumnList.get(j));
                    if (cell != null) {
                        String sourceContent = cell.toString();
                        Document document = documentList.get(j);
                        Element itemElement = document.createElement("string");
                        itemElement.setAttribute("name", key);
                        for (int r = 0; r < mTranslateConfig.mReplacedValueList.size(); r++) {
                            String replacedValue = mTranslateConfig.mReplacedValueList.get(r);
                            String newValue = mTranslateConfig.mNewValueList.get(r);
                            sourceContent = sourceContent.replaceAll(replacedValue, newValue);
                        }
                        itemElement.setTextContent(sourceContent);
                        elementList.get(j).appendChild(itemElement);
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            for (int i = 0; i < languageCodeSize; i++) {
                Document document = documentList.get(i);
                document.appendChild(elementList.get(i));
                transformer.transform(new DOMSource(document), new StreamResult(new File(mTranslateConfig.mOutPath + "\\" + mTranslateConfig.mLanguageCodeList.get(i) + ".xml")));
            }
            //The output is a translation that failed to match
            Document failedDocument = documentBuilder.newDocument();
            failedDocument.setXmlStandalone(true);
            Element failedRootElement = failedDocument.createElement("resources");
            for (Pair itemPair : compareList) {
                if (itemPair == null) continue;
                printLog("Translation matching failed value=" + itemPair.sourceValue);
                Element itemElement = failedDocument.createElement("string");
                itemElement.setAttribute("name", itemPair.key);
                itemElement.setTextContent(itemPair.sourceValue);
                failedRootElement.appendChild(itemElement);
            }
            failedDocument.appendChild(failedRootElement);
            transformer.transform(new DOMSource(failedDocument), new StreamResult(new File(mTranslateConfig.mOutPath + "\\failed.xml")));
            xssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Pair {
        public String key;
        //Store filtered translations
        public String value;
        //Store translations before filtering
        public String sourceValue;

        public Pair(String key, String value, String sourceValue) {
            this.key = key;
            this.value = value;
            this.sourceValue = sourceValue;
        }
    }
}
