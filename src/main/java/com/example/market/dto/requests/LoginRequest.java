package com.example.market.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}