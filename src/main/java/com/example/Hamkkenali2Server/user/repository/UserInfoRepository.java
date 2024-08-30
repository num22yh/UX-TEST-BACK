package com.example.Hamkkenali2Server.user.repository;

import com.example.Hamkkenali2Server.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUserName(String userName);

    boolean existsByUserName(String userName);
}