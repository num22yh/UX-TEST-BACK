package com.example.Hamkkenali2Server.user.entity;
// UserInfo.java

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.Entity;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userinfo")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long userId;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "SearchTerm")
    private String searchTerm;

    @Column(name = "GptTerm")
    private String gptTerm;

    @Column(name = "GptTime")
    private String gptTime;

    @Column(name = "SearchTime")
    private String searchTime;

    @Column(name = "ChatbotTime")
    private String chatbotTime;

}
