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
        // 사용자가 입력한 데이터를 데이터베이스에 저장
        saveInputToDatabase(request.getUserName(), request.getText());

        // 번역된 텍스트 얻기
        String translatedText = translateService.translateKtoE(request.getText(), request.getTargetLanguage());

        // GPT API에 전송할 데이터 구성
        ChatCompletionDto chatCompletionDto = ChatCompletionDto.builder()
                .model("ft:gpt-3.5-turbo-1106:personal::8w8KAYsH")
                .messages(Collections.singletonList(new ChatRequestMsgDto("user", translatedText)))
                .build();

        // GPT API에 전송 및 응답 받기
        Map<String, Object> gptResponse = chatGPTService.prompt(chatCompletionDto);

        // GPT API의 응답 중에서 생성된 새로운 대화 응답 획득
        String gptGeneratedResponse = extractGeneratedResponse(gptResponse);

        // 번역된 텍스트 얻기
        String translatedText2 = translateService.translateEtoK(gptGeneratedResponse, request.getTargetLanguage());

        return translatedText2;
    }

    private String extractGeneratedResponse(Map<String, Object> gptResponse) {
        // 예상되는 GPT API 응답 형식에서 choices 키를 추출
        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.get("choices");

        if (choices != null && !choices.isEmpty()) {
            // choices 배열에서 첫 번째 요소의 message 맵 추출
            Map<String, Object> firstChoice = choices.get(0);

            // message 맵에서 content 키의 값을 추출하여 반환
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            if (message != null) {
                return (String) message.get("content");
            }
        }

        // 기본적으로 빈 문자열을 반환하거나, 오류 메시지를 반환할 수 있음
        return "No response from GPT API";
    }

    private void saveInputToDatabase(String username, String gptTerm) {
        // 사용자 정보 조회
        UserInfo userInfo = userInfoService.findByUserName(username);
        if (userInfo != null) {
            // 기존의 SearchTerm에 새로운 SearchTerm을 추가하여 저장
            String existingGptTerm = userInfo.getGptTerm();
            String toSaveTerm = gptTerm; // 새로운 변수에 새로운 검색어 할당
            if(existingGptTerm != null && !existingGptTerm.isEmpty()) {
                toSaveTerm = existingGptTerm + ", " + gptTerm; // 기존 검색어와 새로운 검색어를 합침
            }
            userInfo.setGptTerm(toSaveTerm); // 새로운 변수를 데이터베이스에 저장할 변수로 사용
            userInfoService.saveUserInfo(userInfo);
        }
    }
}
