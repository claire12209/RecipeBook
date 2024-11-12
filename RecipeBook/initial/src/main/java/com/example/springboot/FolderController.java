package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserService userService;  // Assuming there's a UserService to fetch user data

    // Endpoint to create a new folder
    @PostMapping("/create")
    public ModelAndView createFolder(@RequestParam String folderName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get logged in username
        User user = userService.findByUsername(username); // Assuming you can fetch user by username

        folderService.createFolder(user.getId(), folderName);
        return new ModelAndView("redirect:/profile");  // Redirect to the profile page after folder creation
    }

    // Endpoint to get all folders by user ID
    @GetMapping("/user")
    public ModelAndView getAllFoldersByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        List<Folder> folders = folderService.getAllFoldersByUser(user.getId());
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("folders", folders);
        return mav;
    }
}
