package com.spring.study.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name= "carts")//@Table 어노테이션을 사용하여 db 에 연결..!
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가값이라고 명시해줌
    private int id;

    @Column
    private int memberId;

    @Column
    private int itemId;
}
