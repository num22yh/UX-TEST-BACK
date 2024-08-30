package com.example.Hamkkenali2Server.conversationalChatbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TranslateRequestDTO {
    private String text;
    private String targetLanguage;

    private String userName;

}