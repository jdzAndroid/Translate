package com.jdzAndroid.Translate.bean;

public class Pair {
    public String key;
    //Store filtered translations
    public String value;
    //Store translations before filtering
    public String sourceValue;

    public Pair(String key, String value, String sourceValue) {
        this.key = key;
        this.value = value;
        this.sourceValue = sourceValue;
    }
}
