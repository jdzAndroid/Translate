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
import java.util.Collections;
import java.util.Comparator;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReplaceKey {
    private final String TAG = "ReplaceKey=======";
    private final ReplaceKeyConfig mReplaceKeyConfig;
    private final LinkedBlockingQueue<File> mChildFileList = new LinkedBlockingQueue<>();
    private int mTotalFileCount = 0;
    private final AtomicInteger mCompleteFileCount = new AtomicInteger(0);

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
                Node oldKeyNode = itemNode.getAttributes().getNamedItem(mReplaceKeyConfig.mOldKeyNodeName);
                Node newKeyNode = itemNode.getAttributes().getNamedItem(mReplaceKeyConfig.mNewKeyNodeName);
                if (oldKeyNode == null || newKeyNode == null) continue;
                String oldKeyValue = oldKeyNode.getNodeValue();
                String newKeyValue = newKeyNode.getNodeValue();
                if (oldKeyValue != null && oldKeyValue.length() > 0 && newKeyValue != null && newKeyValue.length() > 0) {
                    keyPairList.add(new KeyPair(preFix.concat(oldKeyValue), preFix.concat(newKeyValue),oldKeyValue,newKeyValue));
                    keyPairList.add(new KeyPair(String.format(prefix_xml, oldKeyValue), String.format(prefix_xml, newKeyValue),oldKeyValue,newKeyValue));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keyPairList.size() == 0) {
            System.out.println(TAG.concat("not found valid oldKeyValue and newKeyValue"));
            return;
        }
        keyPairList.sort((previous, next) -> -(previous.mOldKey.length()-next.mOldKey.length()));
        for (String itemFilePath : mReplaceKeyConfig.mCodeFilePathList) {
            resolveFile(itemFilePath);
        }
        mTotalFileCount=mChildFileList.size();
        if (mTotalFileCount==0){
            System.out.println(TAG.concat("not found any file!!!"));
            return;
        }
        mCompleteFileCount.set(0);
        reallyStartReplaceKey(keyPairList);
    }

    private void resolveFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isFile()) {
                    mChildFileList.add(file);
                } else {
                    File[] fileArray = file.listFiles();
                    assert fileArray != null;
                    for (File itemFile : fileArray) {
                        if (itemFile.isFile()) {
                            mChildFileList.add(itemFile);
                        } else {
                            resolveFile(itemFile.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reallyStartReplaceKey(List<KeyPair> keyList) {
        System.out.println(TAG.concat("has ").concat(String.valueOf(mTotalFileCount).concat(" file will be replaced")));
        while (!mChildFileList.isEmpty()){
            replaceKey(mChildFileList.poll(), keyList);
        }
    }

    private void replaceKey(File file, List<KeyPair> keyList) {
        try {
            System.out.println(TAG.concat("start replace file ").concat(file.getAbsolutePath()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String fileName = file.getName();
            int lastIndex = fileName.lastIndexOf(".");
            String fileEndFix = fileName.substring(lastIndex);
            fileName = fileName.substring(0, lastIndex);
            File outPutFile = new File(file.getParentFile().getAbsolutePath(), fileName.concat("_").concat(String.valueOf(System.currentTimeMillis())).concat(fileEndFix));
            if (outPutFile.exists()) outPutFile.delete();
            if (outPutFile.createNewFile()) {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutFile)));
                StringBuilder builder = new StringBuilder();
                String line = null;
                String lineSeparator = "\n";
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                    builder.append(lineSeparator);
                }
                bufferedReader.close();
                String content = builder.toString();
                if (content.length() > 0) {
                    for (KeyPair itemKeyPair : keyList) {
                        content = content.replaceAll(itemKeyPair.mOldKey, itemKeyPair.mNewKey);
                    }
                }
                bufferedReader.close();
                bufferedWriter.write(content);
                bufferedWriter.flush();
                bufferedWriter.close();
                if (file.delete()) {
                    outPutFile.renameTo(file);
                    System.out.println(TAG.concat("File:").concat(file.getAbsolutePath()).concat(" replace complete"));
                }
                else {
                    System.out.println(TAG.concat("File:").concat(file.getAbsolutePath()).concat(" replace error"));
                }
                mCompleteFileCount.incrementAndGet();
                System.out.println(TAG.concat("File Replace Progress======").concat(String.valueOf(mCompleteFileCount)).concat("/").concat(String.valueOf(mTotalFileCount))
                        .concat(",left").concat(String.valueOf(mTotalFileCount-mCompleteFileCount.get())).concat(" file waited to be replaced"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class KeyPair {
        public String mOldKey;
        public String mNewKey;
        public String mOriginalOldKey;
        public String mOriginalNewKey;

        public KeyPair(String oldKey, String newKey,String originalOldKey,String originalNewKey) {
            mOldKey = oldKey;
            mNewKey = newKey;
            mOriginalOldKey=originalOldKey;
            mOriginalNewKey=originalNewKey;
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
