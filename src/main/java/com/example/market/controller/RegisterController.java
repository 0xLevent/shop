package com.example.market.controller;

import com.example.market.dto.requests.RegisterRequest;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@CrossOrigin(origins = "http://localhost:8282", allowCredentials = "true" )
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest registerRequest, RedirectAttributes redirectAttributes) {
        try {
            userService.register(registerRequest);
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}