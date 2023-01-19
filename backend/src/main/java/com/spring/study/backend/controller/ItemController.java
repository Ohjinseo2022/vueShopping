package com.spring.study.backend.controller;

import com.spring.study.backend.entity.Item;
import com.spring.study.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/api/items")
    @CrossOrigin(origins = "http://localhost:3000/")
    public List<Item> getItems(){
        List<Item> items = itemRepository.findAll();
        return items;
    }
}
