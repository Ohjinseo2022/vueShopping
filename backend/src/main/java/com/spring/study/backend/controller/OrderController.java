package com.spring.study.backend.controller;

import com.spring.study.backend.dto.OrderDto;
import com.spring.study.backend.entity.Order;
import com.spring.study.backend.repository.CartRepository;
import com.spring.study.backend.repository.OrderRepository;
import com.spring.study.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    JwtService jwtService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartRepository cartRepository;

    @GetMapping("/api/orders")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity getOrder(
                                    @CookieValue(value = "token",required = false) String token) {
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        List<Order> orders = orderRepository.findAll();

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

        @Transactional
        @PostMapping("/api/orders")
        @CrossOrigin(origins = "http://localhost:3000/")
        public ResponseEntity pushOrder (@RequestBody OrderDto dto,
                @CookieValue(value = "token", required = false) String token){
            if (!jwtService.isValid(token)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            int memberId = jwtService.getId(token);
            try{
                Order newOrder = new Order();
                newOrder.setMemberId(memberId);
                newOrder.setName(dto.getName());
                newOrder.setAddress(dto.getAddress());
                newOrder.setPayment(dto.getPayment());
                newOrder.setCardNumber(dto.getCardNumber());
                newOrder.setItems(dto.getItems());

                orderRepository.save(newOrder);
                cartRepository.deleteByMemberId(memberId);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }

        }


    }
