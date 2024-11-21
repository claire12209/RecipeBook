package com.example.springboot; 

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RecipeRepository recipeRepository) {
        this.reviewRepository = reviewRepository;
        this.recipeRepository = recipeRepository;
    }

    // Add a review to a recipe
    public void addReview(String comment, Long recipeId, Authentication authentication) {
        // Get the currently logged-in user, or allow null if not authenticated
        User currentUser = (authentication != null) ? (User) authentication.getPrincipal() : null;

        // Find the recipe by its ID
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new RuntimeException("Recipe not found"));

        // Create and populate the review
        Review review = new Review();
        review.setComment(comment);
        review.setRecipe(recipe);

        // Set the user only if it's not null
        if (currentUser != null) {
            review.setUser(currentUser);
        }

        // Save the review to the database
        reviewRepository.save(review);
    }

    // Save a review directly (generic method)
    public void save(Review review) {
        reviewRepository.save(review);
    }

    // Optional method to fetch reviews by recipe
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }
}
