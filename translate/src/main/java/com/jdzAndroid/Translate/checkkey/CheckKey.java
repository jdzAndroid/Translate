package com.jdzAndroid.Translate.checkkey;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CheckKey {
    private CheckKeyConfig mCheckKeyConfig;

    public CheckKey(CheckKeyConfig checkKeyConfig) {
        mCheckKeyConfig = checkKeyConfig;
    }

    public void startCheck() {
        if (mCheckKeyConfig == null) {
            System.out.println("please config check key config");
            return;
        }
        if (mCheckKeyConfig.filePathList.size() == 0) {
            System.out.println("please config check file path");
            return;
        }
        if (mCheckKeyConfig.errorInfoPath == null) {
            System.out.println("please config error info out file path");
            return;
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document errorDocument = documentBuilder.newDocument();
            errorDocument.setXmlStandalone(true);
            Element errorRootElement = errorDocument.createElement("resources");
            Map<String, Map<String, String>> valueMap = new HashMap<>();
            Map<String, String> totalMap = new HashMap<>();
            for (String filePath : mCheckKeyConfig.filePathList) {
                resolve(documentBuilder, filePath, valueMap, totalMap);
            }
            Set<String> valueMapKeySet = valueMap.keySet();
            for (String keyValue : totalMap.keySet()) {
                for (String filePath : valueMapKeySet) {
                    Map<String, String> itemMap = valueMap.get(filePath);
                    if (!itemMap.containsKey(keyValue)) {
                        Element element = errorDocument.createElement("errorInfo");
                        element.setAttribute("filePath", filePath);
                        element.setAttribute("name", keyValue);
                        element.setTextContent("");
                        errorRootElement.appendChild(element);
                    }
                }
            }
            File file = new File(mCheckKeyConfig.errorInfoPath);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            errorDocument.appendChild(errorRootElement);
            transformer.transform(new DOMSource(errorDocument), new StreamResult(new File(mCheckKeyConfig.errorInfoPath, "errorInfo.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resolve(DocumentBuilder documentBuilder, String filePath, Map<String, Map<String, String>> valueMap, Map<String, String> totalMap) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                resolveAndOutPutNewFile(documentBuilder, file, valueMap, totalMap);
            } else {
                File[] fileArray = file.listFiles();
                if (fileArray != null && fileArray.length > 0) {
                    for (File childFile : fileArray) {
                        if (childFile.isFile()) {
                            resolveAndOutPutNewFile(documentBuilder, childFile, valueMap, totalMap);
                        } else {
                            resolve(documentBuilder, childFile.getAbsolutePath(), valueMap, totalMap);
                        }
                    }
                }
            }
        }
    }

    private void resolveAndOutPutNewFile(DocumentBuilder documentBuilder, File file, Map<String, Map<String, String>> valueMap, Map<String, String> totalMap) {
        try {
            org.w3c.dom.Document document = documentBuilder.parse(file);
            String filePath = file.getAbsolutePath();
            NodeList nodeList = document.getElementsByTagName("string");
            int nodeLength = nodeList.getLength();
            Map<String, String> itemMap = new HashMap<>();
            for (int i = 0; i < nodeLength; i++) {
                Node itemNode = nodeList.item(i);
                String KeyValue = itemNode.getAttributes().getNamedItem("name").getNodeValue();
                itemMap.put(KeyValue, filePath);
                totalMap.put(KeyValue, "");
            }
            valueMap.put(filePath, itemMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
