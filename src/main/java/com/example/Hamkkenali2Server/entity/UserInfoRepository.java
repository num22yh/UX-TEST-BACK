package com.example.Hamkkenali2Server.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUserName(String userName);

    boolean existsByUserName(String userName);
}