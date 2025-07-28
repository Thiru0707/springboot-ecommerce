package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private JwtUtil jwtUtil;
    @MockBean private UserService userService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Mockito.when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginUser() throws Exception {
        String username = "testuser";
        String password = "password";
        String token = "mock-jwt-token";

        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));

        Mockito.when(jwtUtil.generateToken(username)).thenReturn(token);

        String loginPayload = """
            {
                "username": "testuser",
                "password": "password"
            }
        """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk());
    }
}

