package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    // Endpoint to create a new folder
    @PostMapping("/create")
    public String createFolder(@RequestParam Long userId, @RequestParam String folderName) {
        return folderService.createFolder(userId, folderName);
    }

    // Endpoint to get all folders by user ID
    @GetMapping("/user/{userId}")
    public List<Folder> getAllFoldersByUser(@PathVariable Long userId) {
        return folderService.getAllFoldersByUser(userId);
    }
}
