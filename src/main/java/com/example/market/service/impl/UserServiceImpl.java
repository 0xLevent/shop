package com.example.market.service.impl;

import com.example.market.dto.requests.RegisterRequest;
import com.example.market.dto.responses.UserResponse;
import com.example.market.entity.Cart;
import com.example.market.entity.User;
import com.example.market.mapper.UserMapper;
import com.example.market.repository.CartRepository;
import com.example.market.repository.UserRepository;
import com.example.market.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final UserMapper userMapper;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           CartRepository cartRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.userMapper = userMapper;
    }


    @Transactional
    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Bu email adresi zaten kullanımda");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("user");

        Cart cart = cartRepository.save(new Cart());
        user.setCart(cart);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }



    @Override
    public UserResponse authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return userMapper.toDto(user); // ← MapStruct kullanımı
        }
        return null;
    }


    @Override
    public User findByEmail(String email) {
        return null;
    }
}