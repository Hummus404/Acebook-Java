package com.makersacademy.acebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/login-using-widget")
    public String loginWidget() {
        return "loginWidget";
    }
    @GetMapping("/callback")
    public String callBack() {
        return "callBack";
    }
    @GetMapping("/after-login")
    public String afterLogin() {
        return "index";
    }
}
