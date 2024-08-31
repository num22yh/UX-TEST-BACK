package com.example.Hamkkenali2Server.search.controller;

import com.example.Hamkkenali2Server.search.entity.CancerInfo;
import com.example.Hamkkenali2Server.user.entity.UserInfo;
import com.example.Hamkkenali2Server.search.service.CancerInfoService;
import com.example.Hamkkenali2Server.user.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<CancerInfo> search(
            @RequestParam(value = "searchTerm") String searchTerm,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // username 검증 로직
        UserInfo userInfo = userInfoService.findByUserName(username);
        if (userInfo != null) {
            String existingSearchTerm = userInfo.getSearchTerm();
            String toSaveTerm = searchTerm;
            if (existingSearchTerm != null && !existingSearchTerm.isEmpty()) {
                toSaveTerm = existingSearchTerm + ", " + searchTerm;
            }
            userInfo.setSearchTerm(toSaveTerm);
            userInfoService.saveUserInfo(userInfo);
        }

        // 페이징된 검색 결과 반환
        return cancerInfoService.searchData(searchTerm, page, size);
    }



    @GetMapping("/document/{contentId}")
    public CancerInfo getDocumentById(@PathVariable int contentId) {

        return cancerInfoService.getDocumentById(contentId);
    }
}
