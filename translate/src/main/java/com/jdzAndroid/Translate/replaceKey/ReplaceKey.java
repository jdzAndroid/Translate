package com.jdzAndroid.Translate.replaceKey;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 从代码中查找指定key并替换成新的Key
 */
public class ReplaceKey {
    private final String TAG = "ReplaceKey=======";
    private ReplaceKeyConfig mReplaceKeyConfig;

    public ReplaceKey(ReplaceKeyConfig mReplaceKeyConfig) {
        this.mReplaceKeyConfig = mReplaceKeyConfig;
    }

    public void replaceKey() {
        if (mReplaceKeyConfig == null) {
            System.out.println(TAG.concat("please add config"));
            return;
        }
        if (mReplaceKeyConfig.mKeyFilePath == null || mReplaceKeyConfig.mKeyFilePath.length() == 0) {
            System.out.println(TAG.concat("please add keyFilePath"));
            return;
        }
        if (mReplaceKeyConfig.mOldKeyNodeName == null || mReplaceKeyConfig.mOldKeyNodeName.length() == 0) {
            System.out.println(TAG.concat("please add oldKeyNodeName"));
            return;
        }
        if (mReplaceKeyConfig.mNewKeyNodeName == null || mReplaceKeyConfig.mNewKeyNodeName.length() == 0) {
            System.out.println(TAG.concat("please add newKeyNodeName"));
            return;
        }
        if (mReplaceKeyConfig.mCodeFilePathList == null || mReplaceKeyConfig.mCodeFilePathList.size() == 0) {
            System.out.println(TAG.concat("please add code file path"));
            return;
        }
        List<KeyPair> keyPairList = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(mReplaceKeyConfig.mKeyFilePath);
            NodeList nodeList = document.getElementsByTagName("string");
            String preFix = "R.string.";
            String prefix_xml = "\"@string/%s\"";
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node itemNode = nodeList.item(i);
                Node oldKeyNode=itemNode.getAttributes().getNamedItem(mReplaceKeyConfig.mOldKeyNodeName);
                Node newKeyNode=itemNode.getAttributes().getNamedItem(mReplaceKeyConfig.mNewKeyNodeName);
                if (oldKeyNode==null||newKeyNode==null)continue;
                String oldKeyValue = oldKeyNode.getNodeValue();
                String newKeyValue = newKeyNode.getNodeValue();
                if (oldKeyValue != null && oldKeyValue.length() > 0 && newKeyValue != null && newKeyValue.length() > 0) {
                    keyPairList.add(new KeyPair(preFix.concat(oldKeyValue), preFix.concat(newKeyValue)));
                    keyPairList.add(new KeyPair(String.format(prefix_xml,oldKeyValue), String.format(prefix_xml,newKeyValue)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keyPairList.size() == 0) {
            System.out.println(TAG.concat("not found valid oldKeyValue and newKeyValue"));
            return;
        }

        for (String itemFilePath : mReplaceKeyConfig.mCodeFilePathList) {
            resolveFile(itemFilePath, keyPairList);
        }
    }

    private void resolveFile(String filePath, List<KeyPair> keyList) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isFile()) {
                    replaceKey(file, keyList);
                } else {
                    File[] fileArray = file.listFiles();
                    assert fileArray != null;
                    for (File itemFile : fileArray) {
                        if (itemFile.isFile()) {
                            replaceKey(itemFile, keyList);
                        } else {
                            resolveFile(itemFile.getAbsolutePath(), keyList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceKey(File file, List<KeyPair> keyList) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String fileName = file.getName();
            int lastIndex = fileName.lastIndexOf(".");
            String fileEndFix = fileName.substring(lastIndex);
            fileName = fileName.substring(0, lastIndex);
            File outPutFile = new File(file.getParentFile().getAbsolutePath(), fileName.concat("_").concat(String.valueOf(System.currentTimeMillis())).concat(fileEndFix));
            if (outPutFile.exists()) outPutFile.delete();
            if (outPutFile.createNewFile()) {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutFile)));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    for (KeyPair itemKeyPair : keyList) {
                        System.out.println("line=".concat(line).concat(",").concat(itemKeyPair.toString()));
                        line = line.replaceAll(itemKeyPair.mOldKey, itemKeyPair.mNewKey);
                    }
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
                bufferedReader.close();
                bufferedWriter.flush();
                bufferedWriter.close();
                file.delete();
                outPutFile.renameTo(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class KeyPair {
        public String mOldKey;
        public String mNewKey;

        public KeyPair(String oldKey, String newKey) {
            mOldKey = oldKey;
            mNewKey = newKey;
        }

        @Override
        public String toString() {
            return "KeyPair{" +
                    "mOldKey='" + mOldKey + '\'' +
                    ", mNewKey='" + mNewKey + '\'' +
                    '}';
        }
    }
}
