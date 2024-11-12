package com.example.springboot;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        user.setEnabled(true); // Enable the user by default
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        userRepository.save(user); // Save the user to the database
    }

    // Method to find a user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}


