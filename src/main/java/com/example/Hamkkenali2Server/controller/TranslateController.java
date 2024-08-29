package com.example.Hamkkenali2Server.controller;

import com.example.Hamkkenali2Server.ChatGPT.ChatCompletionDto;
import com.example.Hamkkenali2Server.ChatGPT.ChatGPTService;
import com.example.Hamkkenali2Server.ChatGPT.ChatRequestMsgDto;
import com.example.Hamkkenali2Server.entity.TranslateRequest;
import com.example.Hamkkenali2Server.entity.UserInfo;
import com.example.Hamkkenali2Server.service.TranslateService;
import com.example.Hamkkenali2Server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    @Autowired
    private TranslateService translateService;

    @Autowired
    private ChatGPTService chatGPTService;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping
    public String translateAndSendToGPT(@RequestBody TranslateRequest request) {

        saveInputToDatabase(request.getUserName(), request.getText());

        String translatedText = translateService.translateKtoE(request.getText(), request.getTargetLanguage());

        ChatCompletionDto chatCompletionDto = ChatCompletionDto.builder()
                .model("ft:gpt-3.5-turbo-1106:personal::8w8KAYsH")
                .messages(Collections.singletonList(new ChatRequestMsgDto("user", translatedText)))
                .build();


        Map<String, Object> gptResponse = chatGPTService.prompt(chatCompletionDto);

        String gptGeneratedResponse = extractGeneratedResponse(gptResponse);

        String translatedText2 = translateService.translateEtoK(gptGeneratedResponse, request.getTargetLanguage());

        return translatedText2;
    }

    private String extractGeneratedResponse(Map<String, Object> gptResponse) {

        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.get("choices");

        if (choices != null && !choices.isEmpty()) {

            Map<String, Object> firstChoice = choices.get(0);

            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            if (message != null) {
                return (String) message.get("content");
            }
        }

        return "No response from GPT API";
    }

    private void saveInputToDatabase(String username, String gptTerm) {

        UserInfo userInfo = userInfoService.findByUserName(username);
        if (userInfo != null) {

            String existingGptTerm = userInfo.getGptTerm();
            String toSaveTerm = gptTerm;
            if(existingGptTerm != null && !existingGptTerm.isEmpty()) {
                toSaveTerm = existingGptTerm + ", " + gptTerm;
            }
            userInfo.setGptTerm(toSaveTerm);
            userInfoService.saveUserInfo(userInfo);
        }
    }
}
