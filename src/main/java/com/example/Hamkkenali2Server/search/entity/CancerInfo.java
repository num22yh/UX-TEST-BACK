package com.example.Hamkkenali2Server.search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cancerinfo")
public class CancerInfo {
    @Id
    @Column(name = "ContentId")
    private int contentId;

    @Column(name = "CancerName")
    private String cancerName;

    @Column(name = "Category")
    private String category;

    @Column(name = "Content")
    private String content;
}



