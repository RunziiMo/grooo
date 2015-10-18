package com.wenym.grooo.http.model;

/**
 * Created by Wouldyou on 2015/5/28.
 */
public class CheckUpdateSuccessData {

    private int version;
    private String content;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CheckUpdateSuccessData{" +
                "version='" + version + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
