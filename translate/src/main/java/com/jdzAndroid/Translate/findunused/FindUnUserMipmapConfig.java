package com.jdzAndroid.Translate.findunused;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiangdz
 * @date 2022/11/7
 * @time 11:53
 * 用途:find unused mipmap
 */
public class FindUnUserMipmapConfig {
    public List<String> codePathList;
    public String errorInfoOutPath;
    public List<String> sourceFilePathList;

    public void codePathList(String... codePath) {
        codePathList = new ArrayList<>();
        if (codePath != null && codePath.length > 0) {
            codePathList.addAll(Arrays.asList(codePath));
        }
    }

    public void errorInfoOutPath(String errorInfoOutPath) {
        this.errorInfoOutPath = errorInfoOutPath;
    }

    public void sourceFilePath(String... sourceFilePath) {
        if (sourceFilePath == null || sourceFilePath.length == 0) return;
        sourceFilePathList = new ArrayList<>();
        sourceFilePathList.addAll(Arrays.asList(sourceFilePath));
    }

    @Override
    public String toString() {
        return "FindUnUserMipmapConfig{" +
                "codePathList=" + codePathList +
                ", errorInfoOutPath='" + errorInfoOutPath + '\'' +
                ", sourceFilePathList=" + sourceFilePathList +
                '}';
    }
}
