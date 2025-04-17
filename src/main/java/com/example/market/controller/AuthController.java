package com.example.market.controller;

import com.example.market.Security.jwt.JwtUtil;
import com.example.market.dto.requests.LoginRequest;
import com.example.market.dto.requests.RegisterRequest;
import com.example.market.dto.responses.GenericResponse;
import com.example.market.dto.responses.JwtResponse;
import com.example.market.dto.responses.UserResponse;
import com.example.market.entity.User;
import com.example.market.service.UserService;
import com.example.market.util.mappers.ModelMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8282", allowCredentials = "true" )
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /*
    @Autowired
    private ModelMapperService modelMapperService;  dosya kaldirilcak
    */


    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<UserResponse>> register(@RequestBody RegisterRequest registerRequest) {
        UserResponse response = userService.register(registerRequest);
        String token = jwtUtil.generateToken(response.getEmail(), response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponse<>(true, "Kullanıcı başarıyla oluşturuldu", response));
    }


    @PostMapping("/login")
    public ResponseEntity<GenericResponse<JwtResponse>> login(@RequestBody LoginRequest loginRequest) {
        UserResponse response = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (response != null) {
            String token = jwtUtil.generateToken(response.getEmail(), response.getId());
            return ResponseEntity.ok(new GenericResponse<>(true, "Giriş başarılı", new JwtResponse(token)));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GenericResponse<>(false, "Geçersiz email veya şifre", null));
        }
    }


    @GetMapping("/check")
    public ResponseEntity<String> checkAuth() {
        return ResponseEntity.ok("Authentication working!");
    }
}