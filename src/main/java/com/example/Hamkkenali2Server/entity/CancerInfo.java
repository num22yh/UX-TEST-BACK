package com.example.Hamkkenali2Server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    @Table(name = "CancerINFO")
    public class CancerInfo {
        @Id
        @Column(name="ContentId")
        private int contentId;

        @Column(name="CancerName")
        private String cancerName;

        @Column(name="Category")
        private String category;

        @Column(name="Content")
        private String content;

    public String getCancerName() {
        return this.cancerName;
    }

    public String getCategory() {
        return this.category;
    }

    public String getContent() {
        return this.content;
    }

    public int getContentId(){
        return this.contentId;
    }


    }

