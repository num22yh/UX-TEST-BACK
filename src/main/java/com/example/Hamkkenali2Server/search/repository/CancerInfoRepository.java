package com.example.Hamkkenali2Server.search.repository;

import com.example.Hamkkenali2Server.search.entity.CancerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CancerInfoRepository extends JpaRepository<CancerInfo, Integer> {
    CancerInfo findByContentId(int contentId);

    @Query("SELECT c FROM CancerInfo c WHERE c.cancerName LIKE %:keyword% OR c.category LIKE %:keyword% OR c.content LIKE %:keyword%")
    List<CancerInfo> searchByKeyword(@Param("keyword") String keyword);
}

