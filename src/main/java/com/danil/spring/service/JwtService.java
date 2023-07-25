package com.danil.spring.service;

import com.danil.spring.model.User;

import io.jsonwebtoken.Claims;

public interface JwtService {
    public String generateToken(User user);

    public Claims getClaims(String token);

    public Integer getUserId(String token);
}
