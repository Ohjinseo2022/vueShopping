package com.spring.study.backend.controller;


import com.spring.study.backend.dto.ContentsDto;
import com.spring.study.backend.entity.Contents;
import com.spring.study.backend.repository.ContentsRepository;
import com.spring.study.backend.repository.NewUserRepository;
import com.spring.study.backend.service.JwtService;
import io.jsonwebtoken.Claims;
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


    // 전체 게시글 확인용도
    @GetMapping("/api/contents/all")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity getContents(){
        List<Contents> contents = contentsRepository.findAll();

        return new ResponseEntity<>(contents, HttpStatus.OK);
    }

    //작성자의 이름 추출해주는 로직
    @GetMapping("/api/contents/name")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity searchUserName(){
        List<Contents> contents = contentsRepository.findAll();
        ArrayList<String> userName = new ArrayList<>();
        for(int i = 0 ;i<contents.size();i++){
            int userIdx = contents.get(i).getUserIdx();
            // idx 값에 맞는 유저 이름을 찾아옴!
            userName.add(newUserRepository.findByIdx(userIdx).getName());
        }

        return new ResponseEntity<>(userName,HttpStatus.OK);
    }


    // 새로운 글 작성
    @PostMapping("/api/addContents")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity addContents(@RequestBody ContentsDto dto,
                                      @CookieValue(value = "token", required = false) String token){
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // 토큰값 복호화
        Claims claims = jwtService.getClaims(token);
        int userIdx = Integer.parseInt(claims.get("idx").toString());
        try {
            Contents newContent = new Contents();
            newContent.setUserIdx(userIdx);
            newContent.setTitle(dto.getTitle());
            newContent.setContents(dto.getContents());
            newContent.setUptime(dto.getUptime());
            contentsRepository.save(newContent);

            return new ResponseEntity<>(HttpStatus.OK);
       }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // 게시글 상세보기
    @GetMapping("/api/detail/content/{idx}")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity detailPage(@PathVariable("idx") int idx){
        try{
            Contents content =contentsRepository.findByIdx(idx);
            return new ResponseEntity<>(content,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }
    // 게시글 삭제
    @DeleteMapping("/api/content/delete/{idx}")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity removeContent(@PathVariable("idx")int idx,
                                        @CookieValue(value="token",required = false)String token){
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // 토큰값 복호화
        Claims claims = jwtService.getClaims(token);
        int userIdx = Integer.parseInt(claims.get("idx").toString());
        Contents content =contentsRepository.findByIdx(idx);
        if(content.getUserIdx() == userIdx){
            contentsRepository.delete(content);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); //권한 없음!
        }
    }

    // 게시글 수정
    @PutMapping("api/content/update")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity updateContent(@RequestBody ContentsDto dto,@CookieValue(value="token",required = false)String token){
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // 토큰값 복호화
        Claims claims = jwtService.getClaims(token);
        int userIdx = Integer.parseInt(claims.get("idx").toString());
        //기존 정보를 가지고옴
        Contents content =contentsRepository.findByIdx(dto.getIdx());

        if(content.getUserIdx() == userIdx){
          content.setTitle(dto.getTitle());
          content.setContents(dto.getContents());
          contentsRepository.save(content);
          return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); //권한 없음!
    }



}
