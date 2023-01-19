package com.spring.study.backend.controller;

import com.spring.study.backend.entity.Member;
import com.spring.study.backend.repository.MemberRepository;
import com.spring.study.backend.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class AccountController {


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("/api/account/login")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity login(@RequestBody Map<String,String> params, HttpServletResponse res){
        Member member = memberRepository.findByEmailAndPassword(params.get("email"),params.get("password"));

        if(member != null){

            int id = member.getId();
            String token = jwtService.getToken("id",id);//아이디 값을 토큰화
            Cookie cookie = new Cookie("token",token);//토큰화된 아이디 cookie 에 적용

            cookie.setHttpOnly(true);//자바스크립트에서 접근 불가능 설정
            cookie.setPath("/");
            res.addCookie(cookie);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        throw new ResponseStatusException(NOT_FOUND);
    }

    @PostMapping("/api/account/logout")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token",null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token",required = false) String token){
        Claims claims = jwtService.getClaims(token);
        if(claims != null){
            int id =Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id,HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.OK);
    }

}
