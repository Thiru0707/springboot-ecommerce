package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    // View profile
    @GetMapping
    public String viewProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "profile";
    }

    // Update profile
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser) {
        userService.updateProfile(updatedUser);
        return "redirect:/profile?success";
    }
}

