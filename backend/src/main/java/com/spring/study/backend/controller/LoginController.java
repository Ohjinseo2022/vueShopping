package com.spring.study.backend.controller;

import com.spring.study.backend.entity.NewUser;
import com.spring.study.backend.repository.NewUserRepository;
import com.spring.study.backend.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class LoginController {

    @Autowired
    NewUserRepository newUserRepository;

    @Autowired
    JwtService jwtService;


    @PostMapping("/api/login")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity login(@RequestBody Map<String,String> params, HttpServletResponse res){

        NewUser user =  newUserRepository.findById(params.get("id"));
        String pa = user.getPassword();
        String[] jwtString = pa.split("\\.");
        String header = jwtString[0];
        String payload = jwtString[1];
        String verify = jwtString[2];

        byte[] payByte= payload.getBytes();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodeBytes = decoder.decode(payByte);
        String result = new String(decodeBytes); // 복호화 완료된 데이터
        BasicJsonParser jsonParser = new BasicJsonParser();
        Map<String, Object> jsonArray = jsonParser.parseMap(result);
        String password = jsonArray.get("password").toString();

        if(user != null && password.equals(params.get("password"))){

            int idx = user.getIdx();
            String token = jwtService.getToken("idx",idx);
            Cookie cookie = new Cookie("token",token);//토큰화된 아이디 cookie 에 적용

            cookie.setHttpOnly(true);//자바스크립트에서 접근 불가능 설정
            cookie.setPath("/");
            res.addCookie(cookie);
            return new ResponseEntity<>(idx, HttpStatus.OK);
        }
        throw new ResponseStatusException(NOT_FOUND);
    }



    @GetMapping("/api/login/check")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity check(@CookieValue(value ="token",required = false) String token){
        Claims claims = jwtService.getClaims(token);
        if(claims != null){
            int idx = Integer.parseInt(claims.get("idx").toString());
            return new ResponseEntity<>(idx, HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    @GetMapping("/api/login/findName")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity findName(@CookieValue(value="token",required = false) String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);//401리턴
        }
        Claims claims = jwtService.getClaims(token);
        int idx = Integer.parseInt(claims.get("idx").toString());
        NewUser user = newUserRepository.findByIdx(idx);
        System.out.println(user.toString());
        String username = user.getName();
        return new ResponseEntity<>(username,HttpStatus.OK);
    }

    @PostMapping("/api/logout")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity logout(HttpServletResponse res){
        Cookie cookie = new Cookie("token",null);

        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
