package com.example.Hamkkenali2Server.controller;

import com.example.Hamkkenali2Server.Exception.DuplicateUsernameException;
import com.example.Hamkkenali2Server.entity.UserInfo;
import com.example.Hamkkenali2Server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/login")
    public UserInfo login(@RequestBody Map<String, String> requestBody) {
        String userName = requestBody.get("username");
        if(userInfoService.existsByUsername(userName)) {
            throw new DuplicateUsernameException("중복된 이름이 존재합니다.");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        return userInfoService.saveUserInfo(userInfo);
    }
    @CrossOrigin
    @GetMapping("/showDB")
    public List<UserInfo> getAllUsers() {
        return userInfoService.getAllUsers();
    }
}
