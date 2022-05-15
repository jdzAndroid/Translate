package com.jdzAndroid.DocTransformation.xml2xls;

import com.jdzAndroid.base.BaseTask;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Tranformation implements BaseTask {
    private TransformationConfig mTransformationConfig;

    public Tranformation(TransformationConfig config) {
        mTransformationConfig = config;
    }

    @Override
    public void startWork() {
        if (!validBasicInfo()) return;
        if (mTransformationConfig.mSourcePathList.size() == 1 && mTransformationConfig.mTargetPathList.size() == 1) {
            oneToone();
        } else if (mTransformationConfig.mSourcePathList.size() == 1 && mTransformationConfig.mTargetPathList.size() > 1) {
            oneTomany();
        } else if (mTransformationConfig.mTargetPathList.size() == 1 && mTransformationConfig.mSourcePathList.size() > 1) {
            manyToone();
        } else {
            manyTomany();
        }
    }

    private void oneToone() {
        try {
            File file = new File(mTransformationConfig.mSourcePathList.get(0));
            if (!file.exists()) {
                printLog("sour file is not exist");
                return;
            }
            File outFile = new File(mTransformationConfig.mTargetPathList.get(0), mTransformationConfig.mTargetFileNameList.get(0) + ".xls");
            if (outFile.exists()) outFile.delete();
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            HSSFSheet hssfSheet = hssfWorkbook.createSheet();
            HSSFRow firstRow = hssfSheet.createRow(0);
            firstRow.createCell(0, CellType.STRING).setCellValue(mTransformationConfig.mKeyList.get(0));
            firstRow.createCell(1, CellType.STRING).setCellValue(mTransformationConfig.mNameList.get(0));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList nodeList = document.getElementsByTagName("string");
            int nodeLength = nodeList.getLength();
            for (int index = 0; index < nodeLength; index++) {
                Element itemNode = (Element) nodeList.item(index);
                String key = itemNode.getAttribute(mTransformationConfig.mKeyList.get(0));
                String value = null;
                if (mTransformationConfig.mValueKeyList == null ||
                        mTransformationConfig.mValueKeyList.size() == 0 ||
                        mTransformationConfig.mValueKeyList.get(0) == null ||
                        mTransformationConfig.mValueKeyList.get(0).replaceAll(" ", "").length() == 0) {
                    value = itemNode.getTextContent();
                } else {
                    value = itemNode.getAttributes().getNamedItem(mTransformationConfig.mValueKeyList.get(0)).getTextContent();
                }
                HSSFRow row = hssfSheet.createRow(index + 1);
                row.createCell(0, CellType.STRING).setCellValue(key);
                row.createCell(1, CellType.STRING).setCellValue(value);
            }
            hssfWorkbook.write(outFile);
            hssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manyToone() {
        File outFile = new File(mTransformationConfig.mTargetPathList.get(0), mTransformationConfig.mTargetFileNameList.get(0) + ".xls");
        if (outFile.exists()) outFile.delete();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow firstRow = hssfSheet.createRow(0);
        firstRow.createCell(0, CellType.STRING).setCellValue(mTransformationConfig.mKeyList.get(0));
        int size=mTransformationConfig.mSourcePathList.size();
        Map<String,Integer>keyLineMap=new HashMap<>();
        for (int i = 0; i < size; i++) {
            try {
                File file = new File(mTransformationConfig.mSourcePathList.get(i));
                if (!file.exists()) {
                    printLog("sour file is not exist");
                    return;
                }
                firstRow.createCell(i+1, CellType.STRING).setCellValue(mTransformationConfig.mNameList.get(i));
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(file);
                NodeList nodeList = document.getElementsByTagName("string");
                int nodeLength = nodeList.getLength();
                for (int index = 0; index < nodeLength; index++) {
                    Element itemNode = (Element) nodeList.item(index);
                    String key = itemNode.getAttribute(mTransformationConfig.mKeyList.get(0));
                    String value = null;
                    if (mTransformationConfig.mValueKeyList == null ||
                            mTransformationConfig.mValueKeyList.size() == 0 ||
                            mTransformationConfig.mValueKeyList.get(0) == null ||
                            mTransformationConfig.mValueKeyList.get(0).replaceAll(" ", "").length() == 0) {
                        value = itemNode.getTextContent();
                    } else {
                        value = itemNode.getAttributes().getNamedItem(mTransformationConfig.mValueKeyList.get(0)).getTextContent();
                    }
                    if (i==0){
                        HSSFRow row = hssfSheet.createRow(index + 1);
                        row.createCell(0, CellType.STRING).setCellValue(key);
                        row.createCell(i+1, CellType.STRING).setCellValue(value);
                        keyLineMap.put(key,index+1);
                    }
                    else {
                        if (keyLineMap.containsKey(key)){
                            HSSFRow row = hssfSheet.createRow(keyLineMap.get(key));
                            row.createCell(i+1, CellType.STRING).setCellValue(value);
                        }
                    }
                }
                hssfWorkbook.write(outFile);
                hssfWorkbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void oneTomany() {

    }

    private void manyTomany() {

    }

    private boolean validBasicInfo() {
        if (mTransformationConfig == null) {
            printLog("config info is null");
            return false;
        }
        if (mTransformationConfig.mSourcePathList == null || mTransformationConfig.mSourcePathList.size() == 0) {
            printLog("source file path is empty!");
            return false;
        }
        if (mTransformationConfig.mTargetPathList == null || mTransformationConfig.mTargetPathList.size() == 0) {
            printLog("target file path is empty!!");
            return false;
        }
        if (mTransformationConfig.mTargetFileNameList == null || mTransformationConfig.mTargetFileNameList.size() == 0) {
            printLog("target file name is empty!!");
            return false;
        }
        if (mTransformationConfig.mKeyList == null || mTransformationConfig.mKeyList.size() == 0) {
            printLog("key name is empty!!");
            return false;
        }
        if (mTransformationConfig.mTargetFileNameList.size() != mTransformationConfig.mTargetPathList.size()) {
            printLog("target file name size not equal target file path size!!");
            return false;
        }
        if (mTransformationConfig.mKeyList.size() != mTransformationConfig.mTargetPathList.size()) {
            printLog("key name size not equal target path size!!");
            return false;
        }
        if (mTransformationConfig.mSourcePathList.size() > 1 && mTransformationConfig.mTargetPathList.size() > 1 &&
                mTransformationConfig.mSourcePathList.size() != mTransformationConfig.mTargetPathList.size()) {
            printLog("only support 1 to many or many to1 or many to many(but size must equal)!!");
            return false;
        }
        return true;
    }

    /**
     * XML 转XLS
     */
    private void xmlToXls() {

    }

    /**
     * XLS to XML
     */
    private void xlsToXml() {

    }
}
