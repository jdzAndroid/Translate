package com.jdzAndroid.Translate.bean;

import java.util.Map;

public class KeyPair {
    //Store sourceKey
    public String sourceKey;
    //Stored filted key
    public String filterKey;
    //Store content
    public String content;
    public Map<String,String> attributeMap;

    public KeyPair(String sourceKey,String filterKey, String content, Map<String,String> attributeMap) {
        this.sourceKey=sourceKey;
        this.filterKey=filterKey;
        this.content=content;
        this.attributeMap = attributeMap;
    }

    @Override
    public String toString() {
        return "KeyPair{" +
                "sourceKey='" + sourceKey + '\'' +
                ", filterKey='" + filterKey + '\'' +
                ", content='" + content + '\'' +
                ", attributeMap=" + attributeMap +
                '}';
    }
}
