package com.example.Hamkkenali2Server.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TranslateRequest {
    private String text;
    private String targetLanguage;

    private String userName;

}