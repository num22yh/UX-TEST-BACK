package com.example.Hamkkenali2Server.service;

import com.example.Hamkkenali2Server.entity.UserInfo;
import com.example.Hamkkenali2Server.entity.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfo saveUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    public UserInfo findByUserName(String userName) {
        return userInfoRepository.findByUserName(userName);
    }

    public boolean existsByUsername(String userName) {
        return userInfoRepository.existsByUserName(userName);
    }

    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }



}