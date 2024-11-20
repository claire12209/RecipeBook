package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe findByIdWithReviews(Long id) {
        // If you added the custom query to fetch recipes with reviews
        return recipeRepository.findByIdWithReviews(id);
    }
    
    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }
    
}
