package com.jdzAndroid.DocTransformation.xml2xls;

import java.util.ArrayList;
import java.util.List;

/**
 *XML TO XLS
 */
public class TransformationConfig {
    //source file path
    public List<String> mSourcePathList;
    //target file path
    public List<String> mTargetPathList;
    //target file name
    public List<String> mTargetFileNameList;
    //XML key name
    public List<String> mKeyList;
    //XML value key name
    public List<String> mValueKeyList;
    //to xls name list
    public List<String> mNameList;

    public void sourcePath(String... sourcePath){
        mSourcePathList=new ArrayList<>();
        if (sourcePath!=null&&sourcePath.length>0){
            for (String path : sourcePath) {
                mSourcePathList.add(path);
            }
        }
    }

    public void targetPath(String... targetPath){
        mTargetPathList=new ArrayList<>();
        if (targetPath!=null&&targetPath.length>0){
            for (String path : targetPath) {
                mTargetPathList.add(path);
            }
        }
    }

    public void targetFileName(String... targetFileName){
        mTargetFileNameList=new ArrayList<>();
        if (targetFileName!=null&&targetFileName.length>0){
            for (String fileName : targetFileName) {
                mTargetFileNameList.add(fileName);
            }
        }
    }

    public void keyName(String... keyName){
        mKeyList=new ArrayList<>();
        if (keyName!=null&&keyName.length>0){
            for (String name : keyName) {
                mKeyList.add(name);
            }
        }
    }

    public void valueName(String... valueName){
        mValueKeyList=new ArrayList<>();
        if (valueName!=null&&valueName.length>0){
            for (String name : valueName) {
                mValueKeyList.add(name);
            }
        }
    }

    public void names(String... names){
        mNameList=new ArrayList<>();
        if (names!=null&&names.length>0){
            for (String itemName : names) {
                mNameList.add(itemName);
            }
        }
    }

}
