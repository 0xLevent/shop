package com.example.market.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
@CrossOrigin(origins = "http://localhost:8282", allowCredentials = "true" )
public class PageController {

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }
    @GetMapping("/shop")
    public String shop() {
        return "shop";
    }
    @GetMapping("/coffees")
    public String coffees() {
        return "coffees";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}

