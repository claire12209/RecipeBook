package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService; // Assuming UserService is available to fetch user details

    @Autowired
    private FolderService folderService; // Service to fetch folders

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get logged-in user's username

        User user = userService.findByUsername(username); // Fetch the user object based on the username
        List<Folder> folders = folderService.getAllFoldersByUser(user.getId()); // Get folders by user ID

        model.addAttribute("username", username);
        model.addAttribute("folders", folders); // Add folders to the model to be displayed in the view

        return "profile"; // Ensure profile.html is set to use these attributes
    }
}
