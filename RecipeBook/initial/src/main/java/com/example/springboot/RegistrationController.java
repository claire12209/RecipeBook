package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Create a new User object for the form
        return "register"; // Return the registration view
    }

    @PostMapping
    public String registerUser(User user) {
        userService.registerUser(user); // Register the user
        return "redirect:/login"; // Redirect to the login page
    }
}
