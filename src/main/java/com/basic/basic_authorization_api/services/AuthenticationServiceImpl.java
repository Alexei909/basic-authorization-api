package com.basic.basic_authorization_api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.basic.basic_authorization_api.dto.JwtResponse;
import com.basic.basic_authorization_api.dto.LoginUserDto;
import com.basic.basic_authorization_api.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;

/**
 * AuthenticationManager проверяет существует ли логин и пароль, 
 * которей приходят в запросе.
 *
 * @author DESKTOP-A37889R
 * @version 
 */
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public JwtResponse login(LoginUserDto dto) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        } catch (BadCredentialsException exception) {
            throw exception;
        }

        UserDetails userDetails = userService.loadUserByUsername(dto.getUsername());
        String accessToken = this.jwtTokenUtils.generateAccessToken(userDetails);

        return new JwtResponse(accessToken);
    }
}
