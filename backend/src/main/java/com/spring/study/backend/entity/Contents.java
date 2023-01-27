package com.spring.study.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
public class Contents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(nullable = false)
    private int userIdx;
    @Column(length = 100,nullable = false)
    private String title;

    @Column(length = 5000,nullable = false)
    private String contents;

    @Column(nullable = false)
    private String uptime;
}
