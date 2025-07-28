package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepository userRepo;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByEmail() {
        User user = new User(); user.setEmail("test@mail.com");
        when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        assertEquals("test@mail.com", userService.getUserByEmail("test@mail.com").getEmail());
    }

    @Test
    void testGetCurrentUser() {
        User user = new User(); user.setUsername("john");
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("john");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        User result = userService.getCurrentUser();
        assertEquals("john", result.getUsername());
    }

    @Test
    void testUpdateProfile() {
        User existing = new User(); existing.setName("Old"); existing.setEmail("old@mail.com");
        User updated = new User(); updated.setName("New"); updated.setEmail("new@mail.com");

        when(userRepo.findByUsername("john")).thenReturn(Optional.of(existing));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("john");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        userService.updateProfile(updated);

        verify(userRepo).save(existing);
        assertEquals("New", existing.getName());
        assertEquals("new@mail.com", existing.getEmail());
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.registerUser(user);

        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("ROLE_USER", savedUser.getRole());
        verify(userRepo).save(user);
    }
}


