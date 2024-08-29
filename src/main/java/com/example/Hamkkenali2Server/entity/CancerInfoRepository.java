package com.example.Hamkkenali2Server.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CancerInfoRepository extends JpaRepository<CancerInfo, Integer> {

    CancerInfo findByContentId(int contentId);

    List<CancerInfo> findByCancerNameAndCategoryAndContent(String cancerName, String category, String content);
    List<CancerInfo> findByCancerName(String token);
    List<CancerInfo> findByCategory(String token);
    List<CancerInfo> findByContentContains(String token);

    
    List<CancerInfo> findByCancerNameOrCategoryOrContentContains(String cancerName, String category, String content);






}
