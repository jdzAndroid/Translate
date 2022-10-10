package com.jdzAndroid.Translate.replaceKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReplaceKeyConfig {
    public String mKeyFilePath;
    public String mOldKeyNodeName;
    public String mNewKeyNodeName;
    public List<String> mCodeFilePathList;
    public int mWorkThreadCount=3;

    public void keyFilePath(String keyFilePath){
        mKeyFilePath=keyFilePath;
    }

    public void oldKeyName(String oldKeyName){
        mOldKeyNodeName=oldKeyName;
    }

    public void newKeyName(String newKeyName){
        mNewKeyNodeName=newKeyName;
    }

    public void codeFilePath(String... codeFilePath){
        if (codeFilePath==null||codeFilePath.length==0)return;
        mCodeFilePathList=new ArrayList<>();
        mCodeFilePathList.addAll(Arrays.asList(codeFilePath));
    }

    public void workThreadCount(int workThreadCount){
        if (workThreadCount<=0)return;
        mWorkThreadCount=workThreadCount;
    }
}
