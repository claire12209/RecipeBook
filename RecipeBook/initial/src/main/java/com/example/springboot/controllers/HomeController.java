package com.example.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    // Home page is working
    @GetMapping("/")
    public String home() {
        logger.info("Accessing home page");
        return "home"; // this is returning the home.html
    }

    //login page is not working
    @GetMapping("/login")
    public String login() {
        logger.info("Accessing login page");
        return "login"; // this should return login.html but isn't...
    }

    //register page is working
    @GetMapping("/register")
    public String register() {
        logger.info("Accessing registration page");
        return "register"; // This does return the register.html
    }
}
