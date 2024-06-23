package com.basic.basic_authorization_api.services;

import com.basic.basic_authorization_api.dto.JwtDto;
import com.basic.basic_authorization_api.dto.LoginUserDto;
import com.basic.basic_authorization_api.dto.RefreshTokenDto;
import com.basic.basic_authorization_api.dto.RegisterUserDto;
import com.basic.basic_authorization_api.exceptions.PasswordMismatchError;
import com.basic.basic_authorization_api.exceptions.UniquenessError;

import jakarta.security.auth.message.AuthException;

public interface AuthenticationService {

    JwtDto login(LoginUserDto dto);
    void registration(RegisterUserDto dto) throws UniquenessError, PasswordMismatchError;
    JwtDto refresh(RefreshTokenDto dto) throws AuthException;

}
