package com.example.Hamkkenali2Server.entity;
// UserInfo.java

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.Entity;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userINFO")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략을 IDENTITY로 변경
    @Column(name = "UserId")
    private Long userId;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "SearchTerm")
    private String searchTerm;

    @Column(name = "GptTerm") // 사용자가 입력한 텍스트를 저장하는 필드 추가
    private String gptTerm;

    @Column(name = "GptTime")
    private String gptTime;

    @Column(name = "SearchTime")
    private String searchTime;

    @Column(name = "ChatbotTime")
    private String chatbotTime;

    // 생성자, 게터, 세터 생략
}
