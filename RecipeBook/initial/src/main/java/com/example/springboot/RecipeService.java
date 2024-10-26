package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Adds a new recipe to the database.
     * @param recipe The recipe to be added.
     * @return The saved recipe.
     */
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    /**
     * Retrieves recipes based on the category name.
     * @param categoryName The name of the category to filter recipes by.
     * @return A list of recipes in the specified category.
     */
    public List<Recipe> findRecipesByCategory(String categoryName) {
        return recipeRepository.findByCategoriesName(categoryName);
    }

    /**
     * Retrieves all available categories for frontend display or filtering.
     * @return A list of predefined categories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
