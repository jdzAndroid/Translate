package com.jdzAndroid.Translate.checkkey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckKeyConfig {
    public List<String> filePathList=new ArrayList<>();
    public String errorInfoPath;

    public void checkFile(String... checkFile){
        filePathList.clear();
        if (checkFile!=null&&checkFile.length>0){
            filePathList.addAll(Arrays.asList(checkFile));
        }
    }

    public void errorInfoFile(String errorFile){
        this.errorInfoPath=errorFile;
    }
}
