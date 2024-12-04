package com.example.springboot;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final ReviewService reviewService;
    private final UserService userService;

    // Constructor for dependency injection
    public RecipeController(RecipeService recipeService, ReviewService reviewService, UserService userService) {
        this.recipeService = recipeService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    // Method to handle the submission of a new review
    @PostMapping("/recipe/{id}/addReview")
    public String addReview(@PathVariable Long id,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            Principal principal) {
        // Get the logged-in user
        User user = userService.findByUsername(principal.getName());

        // Find the recipe by its ID
        Recipe recipe = recipeService.findById(id);

        // Create a new review and populate its fields
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setDate(LocalDateTime.now());
        review.setUser(user);
        review.setRecipe(recipe);

        // Save the review
        reviewService.save(review);

        // Redirect back to the recipe details page
        return "redirect:/recipe/" + id;
    }
}
