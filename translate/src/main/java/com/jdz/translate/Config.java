package com.jdz.translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    public String mExcelFilePath;
    //The national language code separator must be written in front of the national language code if it needs to be defined
    public String mLanguageCodeSplitChar = ",";
    //Country language code to be translated
    public List<String> mLanguageCodeList = new ArrayList<>();
    //Translation file data path
    public String mOutPath;
    //The language translation list separator needs to be customized and must be written in front of the translation list
    public String mColumnSplitChar = ",";
    //Country translation corresponds to the column number of Excel file, counting from 0
    public List<Integer> mColumnList = new ArrayList<>();
    //Ignore the comparison list separator. If you need to customize it, you must write it in front of the text comparison character list
    public String mIgnoreSplitChar = ",";
    //Some characters that need to be filtered out when comparing whether the text content is equal
    public List<String> mIgnoreValueList = new ArrayList<>();
    //Sample XML file used to compare lookup keys
    public String mCompareFilePath;
    //Sample XML file used to compare lookup keys
    public int mStartLine = 0;
    //Take the translation from the end of the first line of the source file, including the current line
    public int mEndLine = -1;
    //Replaced content separator
    public String mReplacedValueSplitChar = ",";
    //List of contents to be replaced
    public List<String> mReplacedValueList=new ArrayList<>();
    //Replaced content list separator
    public String mNewValueSplitChar = ",";
    //Replaced content list
    public List<String> mNewValueList=new ArrayList<>();


    public void excelFilePath(String excelFilePath) {
        mExcelFilePath = excelFilePath;
    }

    public void languageCode(String filePath) {
        if (filePath != null) {
            if (filePath.contains(mLanguageCodeSplitChar)) {
                String[] split = filePath.split(mLanguageCodeSplitChar);
                mLanguageCodeList.addAll(Arrays.asList(split));
            } else {
                mLanguageCodeList.add(filePath);
            }
        }
    }

    public void column(String column) {
        if (column != null) {
            if (column.contains(mColumnSplitChar)) {
                String[] split = column.split(mColumnSplitChar);
                for (String value : split) {
                    mColumnList.add(Integer.parseInt(value));
                }
            } else {
                mColumnList.add(Integer.parseInt(column));
            }
        }
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

    public void compareFilePath(String compareFilePath) {
        mCompareFilePath = compareFilePath;
    }

    public void outPath(String outPath) {
        if (outPath.endsWith("\\")) outPath = outPath.substring(0, outPath.length() - 1);
        mOutPath = outPath;
    }

    public void languageCodeSplit(String splitChar) {
        mLanguageCodeSplitChar = splitChar;
    }

    public void columnSplit(String splitChar) {
        mColumnSplitChar = splitChar;
    }

    public void ignoreSplit(String splitChar) {
        mIgnoreSplitChar = splitChar;
    }

    public void startLine(int startLine) {
        mStartLine = startLine;
    }

    public void endLine(int endLine) {
        mEndLine = endLine;
    }

    public void replacedValueSplit(String replacedValueSplit){
        mReplacedValueSplitChar=replacedValueSplit;
    }

    public void newValueSplit(String newValueSplit){
        mNewValueSplitChar=newValueSplit;
    }

    public void replacedValue(String replacedValue){
        System.out.println(replacedValue);
        if (replacedValue!=null&&replacedValue.length()>0){
            if (replacedValue.contains(mReplacedValueSplitChar)){
                mReplacedValueList.addAll(Arrays.asList(replacedValue.split(mReplacedValueSplitChar)));
            }
            else {
                mReplacedValueList.add(replacedValue);
            }
        }
    }

    public void newValue(String newValue){
        System.out.println(newValue);
        if (newValue!=null&&newValue.length()>0){
            if (newValue.contains(mNewValueSplitChar)){
                mNewValueList.addAll(Arrays.asList(newValue.split(mNewValueSplitChar)));
            }
            else {
                mNewValueList.add(newValue);
            }
        }
    }
}
