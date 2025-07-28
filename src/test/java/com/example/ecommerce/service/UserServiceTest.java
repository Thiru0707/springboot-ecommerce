package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {
    @Mock UserRepository userRepo;
    @InjectMocks UserService userService;

    @Test
    void testGetUserByEmail() {
        User user = new User(); user.setEmail("test@mail.com");
        Mockito.when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        assertEquals("test@mail.com", userService.getUserByEmail("test@mail.com").getEmail());
    }

    @Test
    void testGetCurrentUser() {
        User user = new User(); user.setUsername("john");
        Mockito.when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        // mock SecurityContextHolder
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getName()).thenReturn("john");
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        User result = userService.getCurrentUser();
        assertEquals("john", result.getUsername());
    }

    @Test
    void testUpdateProfile() {
        User existing = new User(); existing.setName("Old"); existing.setEmail("old@mail.com");
        User updated = new User(); updated.setName("New"); updated.setEmail("new@mail.com");

        Mockito.when(userRepo.findByUsername("john")).thenReturn(Optional.of(existing));

        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getName()).thenReturn("john");
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        userService.updateProfile(updated);

        Mockito.verify(userRepo).save(existing);
        assertEquals("New", existing.getName());
        assertEquals("new@mail.com", existing.getEmail());
    }
}
