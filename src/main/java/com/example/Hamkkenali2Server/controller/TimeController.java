package com.example.Hamkkenali2Server.controller;

import com.example.Hamkkenali2Server.entity.UserInfo;
import com.example.Hamkkenali2Server.entity.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class TimeController {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public TimeController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @PostMapping("/saveGptTime")
    public void saveGptTime(@RequestBody Map<String, String> requestData) {
        // 클라이언트로부터 받은 사용자 이름(username)과 경과 시간(elapsedTime)을 추출합니다.
        String username = requestData.get("username");
        String elapsedTime = requestData.get("elapsedTime");

        // 사용자 이름(username)을 기반으로 해당 사용자를 데이터베이스에서 검색합니다.
        UserInfo userInfo = userInfoRepository.findByUserName(username);

        // 검색된 사용자가 존재하고 경과 시간이 유효한 경우에만 처리합니다.
        if (userInfo != null && elapsedTime != null && !elapsedTime.isEmpty()) {
            // 검색된 사용자의 GptTime 필드에 경과 시간을 저장합니다.
            userInfo.setGptTime(elapsedTime);
            userInfoRepository.save(userInfo);
        }
    }

    @PostMapping("/saveSearchTime")
    public void saveSearchTime(@RequestBody Map<String, String> requestData) {
        // 클라이언트로부터 받은 사용자 이름(username)과 경과 시간(elapsedTime)을 추출합니다.
        String username = requestData.get("username");
        String elapsedTime = requestData.get("elapsedTime");

        // 사용자 이름(username)을 기반으로 해당 사용자를 데이터베이스에서 검색합니다.
        UserInfo userInfo = userInfoRepository.findByUserName(username);

        // 검색된 사용자가 존재하고 경과 시간이 유효한 경우에만 처리합니다.
        if (userInfo != null && elapsedTime != null && !elapsedTime.isEmpty()) {
            // 검색된 사용자의 GptTime 필드에 경과 시간을 저장합니다.
            userInfo.setSearchTime(elapsedTime);
            userInfoRepository.save(userInfo);
        }
    }

    @PostMapping("/saveChatbotTime")
    public void saveChatbotTime(@RequestBody Map<String, String> requestData) {
        // 클라이언트로부터 받은 사용자 이름(username)과 경과 시간(elapsedTime)을 추출합니다.
        String username = requestData.get("username");
        String elapsedTime = requestData.get("elapsedTime");

        // 사용자 이름(username)을 기반으로 해당 사용자를 데이터베이스에서 검색합니다.
        UserInfo userInfo = userInfoRepository.findByUserName(username);

        // 검색된 사용자가 존재하고 경과 시간이 유효한 경우에만 처리합니다.
        if (userInfo != null && elapsedTime != null && !elapsedTime.isEmpty()) {
            // 검색된 사용자의 GptTime 필드에 경과 시간을 저장합니다.
            userInfo.setChatbotTime(elapsedTime);
            userInfoRepository.save(userInfo);
        }
    }



}
