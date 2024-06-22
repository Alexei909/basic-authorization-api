package com.basic.basic_authorization_api.services;

import com.basic.basic_authorization_api.dto.JwtResponse;
import com.basic.basic_authorization_api.dto.LoginUserDto;

public interface AuthenticationService {

    JwtResponse login(LoginUserDto dto);
}
