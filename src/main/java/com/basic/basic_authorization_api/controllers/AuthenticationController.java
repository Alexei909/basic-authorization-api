package com.basic.basic_authorization_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basic.basic_authorization_api.dto.ErrorResponse;
import com.basic.basic_authorization_api.dto.JwtDto;
import com.basic.basic_authorization_api.dto.LoginUserDto;
import com.basic.basic_authorization_api.dto.RefreshTokenDto;
import com.basic.basic_authorization_api.dto.RegisterUserDto;
import com.basic.basic_authorization_api.services.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegisterUserDto dto) {
        try {
            this.authenticationService.registration(dto);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(), exception.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("Success registration");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto dto) {
        JwtDto response;
        try {
            response = this.authenticationService.login(dto);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(), "Неверный логин или пароль"),
                    HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenDto dto) {
        JwtDto response = new JwtDto(null, null);
        try {
            response = this.authenticationService.refresh(dto);
        } catch (Exception exception) {
            log.info("     ");
            log.info(exception.getMessage());
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.UNAUTHORIZED.value(), exception.getMessage()),
                    HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(response);
    }

}
