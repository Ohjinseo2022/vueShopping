package com.spring.study.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name ="newuser")
public class NewUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;
    @Column(length = 100,nullable = false, unique = true)
    private String id;
    @Column(length = 45,nullable = false)
    private String name;
    @Column(length = 500,nullable = false)
    private String password;
    @Column(length = 100,nullable = false)
    private String email;
    @Column(length=45,nullable = false)
    private String phone;
}
