package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a new review for a specific recipe by a specific user.
     * @param rating The rating given by the user (1-5).
     * @param comment The comment provided by the user.
     * @param userId The ID of the user leaving the review.
     * @param recipeId The ID of the recipe being reviewed.
     * @return The saved review.
     */
    public Review addReview(int rating, String comment, Long userId, Long recipeId) {
        // Fetch the user and recipe objects
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        // Create a new review and set its fields
        Review review = new Review(rating, comment, user, recipe);
        return reviewRepository.save(review);
    }

    /**
     * Retrieves all reviews for a specific recipe.
     * @param recipeId The ID of the recipe to retrieve reviews for.
     * @return A list of reviews for the specified recipe.
     */
    public List<Review> getReviewsByRecipe(Long recipeId) {
        return reviewRepository.findByRecipeId(recipeId);
    }

    /**
     * Retrieves all reviews by a specific user.
     * @param userId The ID of the user to retrieve reviews for.
     * @return A list of reviews by the specified user.
     */
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}
