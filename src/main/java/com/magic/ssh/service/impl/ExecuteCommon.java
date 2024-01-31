package com.magic.ssh.service.impl;

import com.magic.ssh.service.ExecuteCallable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class ExecuteCommon implements ExecuteCallable {
    ArrayList<String> list = new ArrayList<>();

    private int maxSize = 50;

    private int maxSecond = 120;

    private String stopWords;

    private long startTime;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /*
     * @Description
     * @param maxTime 毫秒
     */
    public void setMaxTime(int maxTime) {
        this.maxSecond = maxTime;
    }

    public void setStopWords(String stopWords) {
        this.stopWords = stopWords;
    }

    public ExecuteCommon() {
        startTime = curTime();
    }

    @Override
    public boolean IamDone() {
        if (!list.isEmpty() && StringUtils.hasText(stopWords)) {
            String[] words = stopWords.split("\\|");
            for (String w : words) {
                if (list.get(list.size() - 1).contains(w)) {
                    return true;
                }
            }

        }
        return (list.size() > maxSize) | (curTime() - startTime > maxSecond * 1000L);
    }

    @Override
    public ExecuteCallable appendBuffer(String content) {
        list.add(content);
        return null;
    }

    @Override
    public String endBuffer() {
        return null;
    }

    public long curTime() {
        return System.currentTimeMillis();
    }
}
