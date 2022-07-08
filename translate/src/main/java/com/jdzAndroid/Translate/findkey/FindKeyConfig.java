package com.jdzAndroid.Translate.findkey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindKeyConfig {
    //Sample XML file used to compare lookup keys
    public int mStartLine = 0;
    //Take the translation from the end of the first line of the source file, including the current line
    public int mEndLine = -1;
    //Excel source file path
    public String mExcelFilePath;
    //Sample XML file used to compare lookup keys
    public String mCompareFilePath;
    //Translation file data path
    public String mOutPath;
    //Ignore the comparison list separator. If you need to customize it, you must write it in front of the text comparison character list
    public String mIgnoreSplitChar = ",";
    //Some characters that need to be filtered out when comparing whether the text content is equal
    public List<String> mIgnoreValueList = new ArrayList<>();
    //compare value column index in source excel file
    public int mCompareIndex;
    //Search for the corresponding translation from the tables in Excel
    public int mExcelSheetIndex=0;

    public void startLine(int startLine) {
        mStartLine = startLine;
    }

    public void endLine(int endLine) {
        mEndLine = endLine;
    }

    public void excelFilePath(String excelFilePath) {
        mExcelFilePath = excelFilePath;
    }

    public void compareFilePath(String compareFilePath) {
        mCompareFilePath = compareFilePath;
    }

    public void outPath(String outPath) {
        if (outPath.endsWith("\\")) outPath = outPath.substring(0, outPath.length() - 1);
        mOutPath = outPath;
    }

    public void ignoreSplit(String splitChar) {
        mIgnoreSplitChar = splitChar;
    }

    public void ignoreValue(String ignoreValue) {
        if (ignoreValue != null) {
            if (ignoreValue.contains(mIgnoreSplitChar)) {
                String[] split = ignoreValue.split(mIgnoreSplitChar);
                mIgnoreValueList.addAll(Arrays.asList(split));
            } else {
                mIgnoreValueList.add(ignoreValue);
            }
        }
    }

    public void compareIndex(int compareIndex) {
        mCompareIndex = compareIndex;
    }

    public void sheetIndex(int sheetIndex){
        mExcelSheetIndex=sheetIndex;
    }
}
