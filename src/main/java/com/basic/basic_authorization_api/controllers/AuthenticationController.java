package com.basic.basic_authorization_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basic.basic_authorization_api.dto.ErrorResponse;
import com.basic.basic_authorization_api.dto.JwtResponse;
import com.basic.basic_authorization_api.dto.LoginUserDto;
import com.basic.basic_authorization_api.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // @PostMapping("/register")
    // public ResponseEntity<?> register(RegisterUserDto dto) {

    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto dto) {
        JwtResponse response;
        try {
            response = this.authenticationService.login(dto);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.UNAUTHORIZED.value(), "Неверный логин или пароль"),
                    HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(response);
    }
}
