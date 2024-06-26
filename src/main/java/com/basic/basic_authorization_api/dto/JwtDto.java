package com.basic.basic_authorization_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JwtDto {

    private String accessToken; 
    private String refreshToken;

}
