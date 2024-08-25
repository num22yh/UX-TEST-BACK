package com.example.Hamkkenali2Server.entity;

public class TranslateRequest {
    private String text;
    private String targetLanguage;

    private String username;

    // Getter and Setter methods
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getUserName(){return username;}

    public void setUsername(String username) {
        this.username = username;
    }
}