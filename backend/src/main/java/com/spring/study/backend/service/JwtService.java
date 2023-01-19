package com.spring.study.backend.service;

import io.jsonwebtoken.Claims;

public interface JwtService {

    String getToken(String key, Object values);

    Claims getClaims(String token);

    boolean isValid(String token);

    int getId(String token);
}
