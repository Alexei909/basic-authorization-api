package com.basic.basic_authorization_api.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.basic.basic_authorization_api.models.Users;

public interface UserService extends UserDetailsService {
    void createNewUser(Users user);
}
