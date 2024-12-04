package com.example.springboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserService userService;  // Assuming there's a UserService to fetch user data

    // Endpoint to create a new folder
    @PostMapping("/create")
    public ModelAndView createFolder(@RequestParam String folderName, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        ModelAndView modelAndView = new ModelAndView("redirect:/profile");

        try {
            String result = folderService.createFolder(user.getId(), folderName);
            if (!"Folder created successfully.".equals(result)) {
                redirectAttributes.addFlashAttribute("errorMessage", result);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace to the console
        }

        return modelAndView;
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