package com.example.market.service;

import com.example.market.dto.requests.RegisterRequest;
import com.example.market.dto.responses.UserResponse;
import com.example.market.entity.User;

public interface UserService {
    UserResponse register(RegisterRequest registerRequest);
    UserResponse authenticate(String email, String password);
    User findByEmail(String email);

}
