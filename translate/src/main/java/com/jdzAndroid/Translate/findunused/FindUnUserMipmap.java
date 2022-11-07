package com.jdzAndroid.Translate.findunused;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jiangdz
 * @date 2022/11/7
 * @time 11:52
 * 用途:find unused mipmap
 */
public class FindUnUserMipmap {
    private FindUnUserMipmapConfig mConfig;

    public FindUnUserMipmap(FindUnUserMipmapConfig config) {
        mConfig = config;
    }

    public void startFind() {
        System.out.println("startFind mConfig="+mConfig.toString());
        if (mConfig == null || mConfig.codePathList == null || mConfig.codePathList.isEmpty() || mConfig.errorInfoOutPath == null ||
                mConfig.sourceFilePathList == null || mConfig.sourceFilePathList.isEmpty()) {
            System.out.println("please config config info");
            return;
        }
        try {
            File file = new File(mConfig.errorInfoOutPath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            file.createNewFile();
            List<FileInfo> fileInfoList = new ArrayList<>();
            List<String> fileList = new ArrayList<>();
            for (String sourceFilePath : mConfig.codePathList) {
                getFilePath(sourceFilePath, fileList);
            }
            for (String sourceFilePath : mConfig.sourceFilePathList) {
                getFileInfo(sourceFilePath, fileInfoList);
            }
            startFound(fileInfoList, fileList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFilePath(String sourceFilePath, List<String> fileList) {
        if (sourceFilePath == null || sourceFilePath.length() == 0) {
            System.out.println("sourceFilePath is empty");
            return;
        }
        File file = new File(sourceFilePath);
        if (file.exists()) {
            if (file.isFile()) {
                fileList.add(file.getAbsolutePath());
            } else {
                File[] childFileList = file.listFiles();
                if (childFileList != null && childFileList.length > 0) {
                    for (File childFile : childFileList) {
                        getFilePath(childFile.getAbsolutePath(), fileList);
                    }
                }
            }
        }
    }

    private void getFileInfo(String filePath, List<FileInfo> fileInfoList) {
        if (filePath == null || filePath.length() == 0) {
            System.out.println("filePath is empty");
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                fileInfoList.add(new FileInfo(file.getName().substring(0, file.getName().lastIndexOf(".")), file.getAbsolutePath()));
            } else {
                File[] childFileList = file.listFiles();
                if (childFileList != null && childFileList.length > 0) {
                    for (File childFile : childFileList) {
                        getFileInfo(childFile.getAbsolutePath(), fileInfoList);
                    }
                }
            }
        }
    }

    private void startFound(List<FileInfo> foundInfoList, List<String> filePathList) {
        if (foundInfoList.isEmpty() || filePathList.isEmpty()) {
            System.out.println("foundInfoMap.size="+foundInfoList.size()+",filePathList.size="+filePathList.size());
            return;
        }
        for (String itemFilePath : filePathList) {
            try {
                StringBuilder builder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(itemFilePath)));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                String content = builder.toString();
                int size=foundInfoList.size();
                for (int index = size-1; index > 0; index--) {
                    String key=foundInfoList.get(index).fileName;
                    if (content.contains("R.mipmap.".concat(key))||content.contains("@mipmap/".concat(key))) {
                        foundInfoList.remove(index);
                        if (foundInfoList.isEmpty()) {
                            break;
                        }
                    }
                }
                if (foundInfoList.isEmpty()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!foundInfoList.isEmpty()){
            for (FileInfo fileInfo : foundInfoList) {
                System.out.println("foundInfoList.size="+foundInfoList.size()+" fileInfo============"+ fileInfo);
            }
        }
    }

    private class FileInfo {
        String fileName;
        String filePath;

        public FileInfo(String fileName, String filePath) {
            this.fileName = fileName;
            this.filePath = filePath;
        }

        @Override
        public String toString() {
            return "FileInfo{" +
                    "fileName='" + fileName + '\'' +
                    ", filePath='" + filePath + '\'' +
                    '}';
        }
    }
}
