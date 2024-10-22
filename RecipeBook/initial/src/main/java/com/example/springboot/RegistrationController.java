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
    public String registerUser(User user, Model model) {
        // Check if the password meets the minimum length requirement
        if (user.getPassword().length() < 8) {
            model.addAttribute("error", "Password must be at least 8 characters long.");
            return "register"; // Return to the registration form with an error message
        }
        
        userService.registerUser(user); // Register the user if validation passes
        return "redirect:/login"; // Redirect to the login page
    }
}
