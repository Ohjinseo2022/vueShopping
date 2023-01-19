package com.spring.study.backend.repository;

import com.spring.study.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Member findByEmailAndPassword(String email, String password);
}
