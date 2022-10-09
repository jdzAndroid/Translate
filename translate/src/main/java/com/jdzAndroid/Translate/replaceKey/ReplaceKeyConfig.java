package com.jdzAndroid.Translate.replaceKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReplaceKeyConfig {
    //Key值列表XNL文件路径
    public String mKeyFilePath;
    //需要被替换的Key值名称
    public String mOldKeyNodeName;
    //新的Key值名称
    public String mNewKeyNodeName;
    //代码所在路径
    public List<String> mCodeFilePathList;

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
}
