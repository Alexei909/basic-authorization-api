package com.basic.basic_authorization_api.services;

import org.springframework.security.core.userdetails.UserDetails;

import com.basic.basic_authorization_api.dto.JwtDto;

import jakarta.security.auth.message.AuthException;

public interface JwtTokenService {

    JwtDto generateTokens(UserDetails userDetails);
    JwtDto refreshTokens(String refreshToken) throws AuthException;

}
