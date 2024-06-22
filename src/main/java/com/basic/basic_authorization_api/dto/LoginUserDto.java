package com.basic.basic_authorization_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginUserDto {

    private String username;
    private String password;
}
