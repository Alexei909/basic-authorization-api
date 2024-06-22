package com.basic.basic_authorization_api.utils;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtilsImpl implements JwtTokenUtils {

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.lifetime}")
    private Duration accessTokenLifeTime;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.lifetime}")
    private Duration refreshTokenLifeTime;

    public String generateAccessToken(UserDetails userDetails) {
        return this.generateToken(userDetails, this.accessTokenSecret, this.accessTokenLifeTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return this.generateToken(userDetails, this.refreshTokenSecret, this.refreshTokenLifeTime);
    }

    public String getUsernameFromAccessToken(String token) {
        return this.getUsernameFromToken(token, this.accessTokenSecret);
    }

    public String getUsernameFromRefreshToken(String token) {
        return this.getUsernameFromToken(token, this.refreshTokenSecret);
    }

    public List<String> getRolesFromAccessToken(String token) {
        return this.getRolesFromToken(token, this.accessTokenSecret);
    }

    public List<String> getRolesFromRefreshToken(String token) {
        return this.getRolesFromToken(token, this.refreshTokenSecret);
    }

    @SuppressWarnings("unchecked")
    private List<String> getRolesFromToken(String token, String secret) {
        return (List<String>) this.getClaims(token, secret)
                .get("roles");
    }

    private String getUsernameFromToken(String token, String secret) {
        return this.getClaims(token, secret)
                .getSubject();
    }

    private Claims getClaims(String token, String secret) {
        return Jwts.parser()
                .verifyWith(getSecretKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private String generateToken(UserDetails userDetails, String secret, Duration lifetime) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .toList();
        claims.put("roles", roles);
        
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(getSecretKey(this.accessTokenSecret))
                .compact();
    }

    private SecretKey getSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
