package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get current authenticated user (without password)
    @GetMapping("/me")
    public UserDTO me(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    // DTO to return only safe fields
    public static class UserDTO {
        private Long id;
        private String username;
        private String email;
        private String role;

        public UserDTO(Long id, String username, String email, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
        }

        // Getters
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}
