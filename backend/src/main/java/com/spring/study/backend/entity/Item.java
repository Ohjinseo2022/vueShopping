package com.spring.study.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
@Entity
@Getter
@Table(name= "items")//@Table 어노테이션을 사용하여 db 에 연결..!
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가값이라고 명시해줌
    private int id;

    @Column(length = 50,nullable = false)
    private String name;
    @Column(length = 200)
    private String imgPath;
    @Column
    private int price;
    @Column
    private int discountPer;
}
