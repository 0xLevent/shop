package com.example.market.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "http://localhost:8282", allowCredentials = "true" )
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}