package com.jdzAndroid.Translate.bean;

import java.util.Map;

public class KeyPair {
    //Store filtered translations
    public String value;
    //Store translations before filtering
    public String sourceValue;
    public Map<String,String> attributeMap;

    public KeyPair(String value, String sourceValue, Map<String,String> attributeMap) {
        this.value = value;
        this.sourceValue = sourceValue;
        this.attributeMap = attributeMap;
    }

    @Override
    public String toString() {
        return "KeyPair{" +
                "value='" + value + '\'' +
                ", sourceValue='" + sourceValue + '\'' +
                ", attributeMap=" + attributeMap +
                '}';
    }
}
