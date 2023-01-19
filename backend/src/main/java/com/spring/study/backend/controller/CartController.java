package com.spring.study.backend.controller;

import com.spring.study.backend.entity.Cart;
import com.spring.study.backend.entity.Item;
import com.spring.study.backend.repository.CartRepository;
import com.spring.study.backend.repository.ItemRepository;
import com.spring.study.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    CartRepository cartRepository;

//    쿠키 값에 저장된 token 정보를 불러옴
    @GetMapping("/api/cart/items")
    public ResponseEntity getCartItems(@CookieValue(value="token",required = false)String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);//401리턴
        }
        int memberId = jwtService.getId(token);
        List<Cart> carts = cartRepository.findByMemberId(memberId);
        List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList();

        List<Item> items = itemRepository.findByIdIn(itemIds);

        return new ResponseEntity<>(items,HttpStatus.OK);
    }

    @PostMapping("/api/cart/items/{itemId}")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity pushCartItem(@PathVariable("itemId") int itemId,
                                   @CookieValue(value = "token",required = false) String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        int memberId = jwtService.getId(token); //로그인 아이디 정보 확인
        Cart cart =  cartRepository.findByMemberIdAndItemId(memberId,itemId);//로그인아이디, 아이템아이디를 기존 db에서 검색
        
        if(cart == null){
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            cartRepository.save(newCart);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/cart/items/{itemId}")
    @CrossOrigin(origins = "http://localhost:3000/")
    public ResponseEntity removeCartItem(@PathVariable("itemId") int itemId,
                                       @CookieValue(value = "token",required = false) String token){

        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        }
        int memberId = jwtService.getId(token);
        Cart cart =  cartRepository.findByMemberIdAndItemId(memberId,itemId);//로그인아이디, 아이템아이디를 기존 db에서 검색
        cartRepository.delete(cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
