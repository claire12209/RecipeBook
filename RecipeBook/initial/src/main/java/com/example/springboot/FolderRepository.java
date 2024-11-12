package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;


public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByNameAndUserId(String name, Long userId); // Method to check for duplicate folder names
    List<Folder> findByUserId(Long userId);
}
