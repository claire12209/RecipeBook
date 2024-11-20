package com.example.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.springboot.Recipe;
import com.example.springboot.RecipeRepository;
import com.example.springboot.Review;
import com.example.springboot.ReviewRepository;
import com.example.springboot.User;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RecipeRepository recipeRepository) {
        this.reviewRepository = reviewRepository;
        this.recipeRepository = recipeRepository;
    }

    public void addReview(String comment, Long recipeId, Authentication authentication) {
        // Get the current user or allow for null
        User currentUser = (authentication != null) ? (User) authentication.getPrincipal() : null;

        // Find the recipe by ID
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new RuntimeException("Recipe not found"));

        // Create and save the review
        Review review = new Review();
        review.setComment(comment);
        review.setRecipe(recipe);
        
        // Set the user only if it's not null (for logged-in users)
        if (currentUser != null) {
            review.setUser(currentUser);
        }

        reviewRepository.save(review); // Save the review to the database
    }

    // New generic save method
    public void save(Review review) {
        reviewRepository.save(review);
    }
}
