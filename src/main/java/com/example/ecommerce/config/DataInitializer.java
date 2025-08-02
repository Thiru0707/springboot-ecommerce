package com.example.ecommerce.config;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .role("ROLE_ADMIN")
                .build();
            userRepository.save(admin);
            System.out.println("✅ Default admin user created: admin / admin123");
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User user = User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user123"))
                .role("ROLE_USER")
                .build();
            userRepository.save(user);
            System.out.println("✅ Default normal user created: user / user123");
        }
    }
}
