package com.basic.basic_authorization_api.utils;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenUtils {

    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
}
