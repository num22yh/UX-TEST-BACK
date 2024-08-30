package com.example.Hamkkenali2Server.service;

//import com.google.api.client.util.Value;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class TranslateService {

    @Value("${google.cloud.apikey}")
    private String apiKey;


    public String translateKtoE(String text, String targetLanguage) {
        Translate translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("en"),Translate.TranslateOption.format("text"));
        return translation.getTranslatedText();
    }

    public String translateEtoK(String text, String targetLanguage) {
        Translate translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("ko"),Translate.TranslateOption.format("text"));
        return translation.getTranslatedText();
    }

}

