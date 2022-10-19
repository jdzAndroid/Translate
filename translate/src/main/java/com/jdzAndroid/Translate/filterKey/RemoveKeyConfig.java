package com.jdzAndroid.Translate.filterKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveKeyConfig {
    public List<String> mFilePathList;
    public List<String> mKeyList;

    public void removeKeyList(String... keyList){
        if (keyList==null||keyList.length==0)return;
        mKeyList=new ArrayList<>();
        mKeyList.addAll(Arrays.asList(keyList));
    }

    public void filePathList(String... filePathList){
        if (filePathList==null||filePathList.length==0)return;
        mFilePathList=new ArrayList<>();
        mFilePathList.addAll(Arrays.asList(filePathList));
    }
}
