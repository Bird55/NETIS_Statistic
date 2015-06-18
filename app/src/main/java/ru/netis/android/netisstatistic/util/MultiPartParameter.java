package ru.netis.android.netisstatistic.util;

public class MultiPartParameter {

    public MultiPartParameter(String name, String contentType, String content) {
        this.name = name;
        this.contentType = contentType;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getName() {
        return name;
    }

    private String name;

    private String contentType;
    private String content;
}

