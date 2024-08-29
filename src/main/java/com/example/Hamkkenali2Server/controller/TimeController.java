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

        String username = requestData.get("username");
        String elapsedTime = requestData.get("elapsedTime");


        UserInfo userInfo = userInfoRepository.findByUserName(username);

        if (userInfo != null && elapsedTime != null && !elapsedTime.isEmpty()) {

            userInfo.setGptTime(elapsedTime);
            userInfoRepository.save(userInfo);
        }
    }

    @PostMapping("/saveSearchTime")
    public void saveSearchTime(@RequestBody Map<String, String> requestData) {

        String username = requestData.get("username");
        String elapsedTime = requestData.get("elapsedTime");


        UserInfo userInfo = userInfoRepository.findByUserName(username);


        if (userInfo != null && elapsedTime != null && !elapsedTime.isEmpty()) {

            userInfo.setSearchTime(elapsedTime);
            userInfoRepository.save(userInfo);
        }
    }

    @PostMapping("/saveChatbotTime")
    public void saveChatbotTime(@RequestBody Map<String, String> requestData) {

        String username = requestData.get("username");
        String elapsedTime = requestData.get("elapsedTime");


        UserInfo userInfo = userInfoRepository.findByUserName(username);


        if (userInfo != null && elapsedTime != null && !elapsedTime.isEmpty()) {

            userInfo.setChatbotTime(elapsedTime);
            userInfoRepository.save(userInfo);
        }
    }



}
