package com.spring.study.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name= "orders")//@Table 어노테이션을 사용하여 db 에 연결..!
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가값이라고 명시해줌
    private int id;

    @Column
    private int memberId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String address;

    @Column(length = 30, nullable = false)
    private String payment;

    @Column(length = 16)
    private String cardNumber;

    @Column(length = 500, nullable = false)
    private String items;
}
