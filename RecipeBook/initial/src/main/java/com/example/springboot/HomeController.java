package com.example.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error) {
        // Add error handling if needed
        if (error != null) {
            // You can set an attribute to display an error message in your view
            System.out.println("Login error occurred"); // Debugging message
        }
        return "login"; // Returns the login.html page
    }
}
