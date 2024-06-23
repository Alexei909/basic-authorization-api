package com.basic.basic_authorization_api.utils;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenUtils {

    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean validateToken(String token, TokenType type);
    String getUsernameFromAccessToken(String token);
    String getUsernameFromRefreshToken(String token);
    List<String> getRolesFromAccessToken(String token);
    List<String>getRolesFromRefreshToken(String token);
    
}
