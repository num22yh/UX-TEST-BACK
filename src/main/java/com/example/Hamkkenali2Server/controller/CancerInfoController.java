package com.example.Hamkkenali2Server.controller;

import com.example.Hamkkenali2Server.entity.CancerInfo;
import com.example.Hamkkenali2Server.entity.UserInfo;
import com.example.Hamkkenali2Server.service.CancerInfoService;
import com.example.Hamkkenali2Server.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CancerInfoController {

    private static final Logger logger = LoggerFactory.getLogger(CancerInfoController.class);

    private final CancerInfoService cancerInfoService;
    private final UserInfoService userInfoService;

    @Autowired
    public CancerInfoController(CancerInfoService cancerInfoService, UserInfoService userInfoService) {
        this.cancerInfoService = cancerInfoService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/search")
    public List<CancerInfo> search(@RequestParam(value = "searchTerm") String searchTerm, @RequestParam(value = "username") String username) {

        UserInfo userInfo = userInfoService.findByUserName(username);
        if (userInfo != null) {

            String existingSearchTerm = userInfo.getSearchTerm();
            String toSaveTerm = searchTerm;
            if(existingSearchTerm != null && !existingSearchTerm.isEmpty()) {
                toSaveTerm = existingSearchTerm + ", " + searchTerm;
            }
            userInfo.setSearchTerm(toSaveTerm);
            userInfoService.saveUserInfo(userInfo);
        }


        return cancerInfoService.searchData(searchTerm);
    }



    @GetMapping("/document/{contentId}")
    public CancerInfo getDocumentById(@PathVariable int contentId) {

        return cancerInfoService.getDocumentById(contentId);
    }
}
