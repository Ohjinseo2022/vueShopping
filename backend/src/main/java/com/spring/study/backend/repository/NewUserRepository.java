package com.spring.study.backend.repository;

import com.spring.study.backend.entity.NewUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewUserRepository extends JpaRepository<NewUser,Integer> {
    NewUser findByIdAndPassword(String id, String password);

    NewUser findById(String id);

    NewUser findByIdx(int idx);

}
