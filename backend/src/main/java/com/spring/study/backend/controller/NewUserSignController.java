package com.spring.study.backend.controller;

import com.spring.study.backend.dto.UserDto;
import com.spring.study.backend.entity.NewUser;
import com.spring.study.backend.repository.NewUserRepository;
import com.spring.study.backend.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewUserSignController {

    @Autowired
    NewUserRepository newUserRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("api/signup")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity pushUser(@RequestBody UserDto dto, HttpServletResponse res){

        String tokenPassword = jwtService.getToken("password",dto.getPassword());
        try{
            // 회원가입 로직
            NewUser newUser = new NewUser();
            newUser.setId(dto.getId());
            newUser.setName(dto.getName());
            newUser.setPassword(tokenPassword);
            newUser.setEmail(dto.getEmail());
            newUser.setPhone(dto.getPhone());

            newUserRepository.save(newUser);
            // 회원가입후 자동 로그인 기능 !
            NewUser user = newUserRepository.findByIdAndPassword(dto.getId(),tokenPassword);
            int idx = user.getIdx();
            String name = user.getName();
            String token = jwtService.getToken("idx",idx);
            Cookie cookie =new Cookie("token",token);
            Cookie cookie1 = new Cookie("name",name);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            res.addCookie(cookie);
            res.addCookie(cookie1);

            return new ResponseEntity<>(idx,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
