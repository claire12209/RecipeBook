package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Ensure this import is present
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Make sure this is imported correctly

    public void registerUser(User user) {
        user.setEnabled(true); // Enable the user by default
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        userRepository.save(user); // Save the user to the database
    }
}

