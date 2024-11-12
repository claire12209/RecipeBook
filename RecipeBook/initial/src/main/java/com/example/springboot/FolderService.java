package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to create a new folder for a user
    public String createFolder(Long userId, String folderName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return "User not found.";
        }

        User user = userOptional.get();
        
        // Check for duplicate folder names
        Optional<Folder> existingFolder = folderRepository.findByNameAndUserId(folderName, userId);
        if (existingFolder.isPresent()) {
            return "Folder name already exists.";
        }

        Folder newFolder = new Folder();
        newFolder.setName(folderName);
        newFolder.setUser(user);

        folderRepository.save(newFolder);
        System.out.println("Folder created successfully.");
        return "Folder created successfully.";
    }

    // Method to get all folders for a user
    public List<Folder> getAllFoldersByUser(Long userId) {
        return folderRepository.findByUserId(userId);
    }

    //deletion of a folder in profile
    public void deleteFolderById(Long folderId) {
        folderRepository.deleteById(folderId);
    }
    
}
