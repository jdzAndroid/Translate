package com.jdzAndroid.Translate.filterKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveKeyConfig {
    //需要移除Key的xml文件所在的路径（可以传目录和指定文件路径）
    public List<String> mFilePathList;
    //需要移除的Key列表
    public List<String> mKeyList;

    public void excludeKeyList(String... keyList){
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
