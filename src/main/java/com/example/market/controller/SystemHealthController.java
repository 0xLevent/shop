package com.example.market.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class SystemHealthController {

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("System is running");
    }


}
