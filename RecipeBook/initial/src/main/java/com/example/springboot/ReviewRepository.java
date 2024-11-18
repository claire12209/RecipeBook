package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find reviews for a particular recipe
    List<Review> findByRecipeId(Long recipeId);
}
