package com.spring.study.backend.controller;


import com.spring.study.backend.dto.ContentsDto;
import com.spring.study.backend.entity.Contents;
import com.spring.study.backend.repository.ContentsRepository;
import com.spring.study.backend.repository.NewUserRepository;
import com.spring.study.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ContentsController {

    @Autowired
    JwtService jwtService;

    @Autowired
    NewUserRepository newUserRepository;

    @Autowired
    ContentsRepository contentsRepository;

    @GetMapping("/api/contents/all")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity getContents(){
        List<Contents> contents = contentsRepository.findAll();

        return new ResponseEntity<>(contents, HttpStatus.OK);
    }
    @GetMapping("/api/contents/name")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity searchUserName(){
        List<Contents> contents = contentsRepository.findAll();
        ArrayList<String> userName = new ArrayList<>();
        for(int i = 0 ;i<contents.size();i++){
            int userIdx = Integer.parseInt(contents.get(i).getUserIdx());
            // idx 값에 맞는 유저 이름을 찾아옴!
            userName.add(newUserRepository.findByIdx(userIdx).getName());
        }

        return new ResponseEntity<>(userName,HttpStatus.OK);
    }

    @PostMapping("/api/addContents")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity addContents(@RequestBody ContentsDto dto,
                                      @CookieValue(value = "token", required = false) String token){
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        int userIdx = jwtService.getId(token);
        try{
            Contents newContent = new Contents();
            newContent.setUserIdx();
        }
    }

}
