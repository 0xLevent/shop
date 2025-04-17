package com.example.market;

import com.example.market.dto.requests.RegisterRequest;
import com.example.market.dto.responses.UserResponse;
import com.example.market.entity.Cart;
import com.example.market.entity.User;
import com.example.market.repository.CartRepository;
import com.example.market.repository.UserRepository;
import com.example.market.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void testRegisterUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");
        request.setName("Test User");

        Cart savedCart = new Cart();
        savedCart.setId(1);

        User savedUser = new User();
        savedUser.setEmail("test@test.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setName("Test User");
        savedUser.setRole("user");
        savedUser.setCart(savedCart);

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.register(request);

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        assertEquals("Test User", result.getName());
        assertEquals("user", result.getRole());
        assertNotNull(result.getCart());
        assertEquals(1, result.getCart().getId());
    }
}