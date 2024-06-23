package com.basic.basic_authorization_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserDto {

    private String username;
    private String password;
    private String confirmPassword;
    private String email;

}
