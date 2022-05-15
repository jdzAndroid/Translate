package com.jdzAndroid.base;

public interface BaseTask {

    default void printLog(String msg) {
        if (msg == null || msg.length() == 0) return;
        System.out.println(msg);
    }

    void startWork();
}
