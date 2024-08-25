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
        // 사용자 이름을 검색하여 데이터베이스에서 검색어 저장
        UserInfo userInfo = userInfoService.findByUserName(username);
        if (userInfo != null) {
            // 기존의 SearchTerm에 새로운 SearchTerm을 추가하여 저장
            String existingSearchTerm = userInfo.getSearchTerm();
            String toSaveTerm = searchTerm; // 새로운 변수에 새로운 검색어 할당
            if(existingSearchTerm != null && !existingSearchTerm.isEmpty()) {
                toSaveTerm = existingSearchTerm + ", " + searchTerm; // 기존 검색어와 새로운 검색어를 합침
            }
            userInfo.setSearchTerm(toSaveTerm); // 새로운 변수를 데이터베이스에 저장할 변수로 사용
            userInfoService.saveUserInfo(userInfo);
        }

        // 검색 결과 반환
        return cancerInfoService.searchData(searchTerm); // searchData 메서드의 파라미터로는 searchTerm을 그대로 전달
    }



    @GetMapping("/document/{contentId}")
    public CancerInfo getDocumentById(@PathVariable int contentId) {
        // 서비스에서 특정 contentId에 해당하는 문서 조회 로직 구현
        return cancerInfoService.getDocumentById(contentId);
    }
}
