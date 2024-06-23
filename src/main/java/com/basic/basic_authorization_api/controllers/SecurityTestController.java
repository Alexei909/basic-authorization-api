package com.basic.basic_authorization_api.controllers;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * SecurityTestController содержит в себе общедоступные и защищенные endpoints.
 *
 * @author DESKTOP-A37889R
 * @version 1
 */
@RestController
@RequestMapping("check-security")
@RequiredArgsConstructor
public class SecurityTestController {

    @GetMapping("public")
    public String publicData() {
        return "Public data";
    }

    @GetMapping("secured")
    public String securedData() {
        return "Secured data";
    }

    @GetMapping("admin") 
    public String adminData() {
        return "Admin data";
    }

    @GetMapping("info")
    public String userInfo(Principal principal) {
        return "Current user is %s"
                .formatted(principal.getName());
    }

}
