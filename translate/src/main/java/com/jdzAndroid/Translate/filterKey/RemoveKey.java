package com.jdzAndroid.Translate.filterKey;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class RemoveKey {
    private final String TAG="RemoveKey======";
    private final RemoveKeyConfig mRemoveKeyConfig;

    public RemoveKey(RemoveKeyConfig removeKeyConfig) {
        mRemoveKeyConfig = removeKeyConfig;
    }

    public void removeKey(){
        if (mRemoveKeyConfig==null){
            System.out.println(TAG.concat("please add config"));
            return;
        }
        if (mRemoveKeyConfig.mKeyList==null||mRemoveKeyConfig.mKeyList.size()==0){
            System.out.println(TAG.concat("please add exclude key list"));
            return;
        }
        if (mRemoveKeyConfig.mFilePathList==null||mRemoveKeyConfig.mFilePathList.size()==0){
            System.out.println(TAG.concat("please add file path list"));
            return;
        }
        try {
            DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            List<Document>documentList=new ArrayList<>();
            for (String filePath : mRemoveKeyConfig.mFilePathList) {
                resolve(documentBuilder,documentList,filePath,mRemoveKeyConfig.mKeyList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void resolve(DocumentBuilder documentBuilder,List<Document> documentList,String filePath,List<String>keyNameList){
        if (filePath==null||filePath.length()==0){
            System.out.println(TAG.concat("file path is empty"));
            return;
        }
        File file=new File(filePath);
        if (file.exists()){
            if (file.isFile()){
                resolveAndOutPutNewFile(documentBuilder,documentList,file,keyNameList);
            }
            else {
                File[] fileArray=file.listFiles();
                if (fileArray!=null&&fileArray.length>0){
                    for (File childFile : fileArray) {
                        if (childFile.isFile()){
                            resolveAndOutPutNewFile(documentBuilder,documentList,childFile,keyNameList);
                        }
                        else {
                            resolve(documentBuilder,documentList,childFile.getAbsolutePath(),keyNameList);
                        }
                    }
                }
            }
        }
    }

    private void resolveAndOutPutNewFile(DocumentBuilder documentBuilder,List<Document>documentList,File file,List<String>keyNameList){
        try {
            org.w3c.dom.Document document = documentBuilder.parse(file);
            NodeList nodeList = document.getElementsByTagName("string");
            org.w3c.dom.Document newDocument=documentBuilder.newDocument();
            Element newElement= newDocument.createElement("resources");
            int nodeLength=nodeList.getLength();
            for (int i=0;i<nodeLength;i++){
                Node itemNode=nodeList.item(i);
                NamedNodeMap attributes = itemNode.getAttributes();
                Element itemElement=newDocument.createElement("string");
                for (String itemKeyName : keyNameList) {
                    attributes.removeNamedItem(itemKeyName);
                }
                itemElement.setTextContent(itemNode.getTextContent());
                int attributesLength=attributes.getLength();
                for (int j = 0; j < attributesLength; j++) {
                    Node itemAttributes=attributes.item(j);
                    itemElement.setAttribute(itemAttributes.getNodeName(),itemAttributes.getNodeValue());
                }
                newElement.appendChild(itemElement);
            }
            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            newDocument.appendChild(newElement);
            String filePath=file.getAbsolutePath();
            filePath=filePath.substring(0,filePath.lastIndexOf(".xml")).concat("_new").concat(".xml");
            System.out.println(filePath);
            transformer.transform(new DOMSource(newDocument), new StreamResult(new File(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
