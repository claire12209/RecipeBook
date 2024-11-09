package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.springboot.User;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Home method to display recipes or search results
    @GetMapping("/home")
    public String home(Model model, @RequestParam(value = "query", required = false) String query) {
        List<Recipe> recipes;

        if (query != null && !query.isEmpty()) {
            recipes = recipeRepository.findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(query, query);
        } else {
            recipes = recipeRepository.findAll();
        }

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);  // Pass categories to the view for the dropdown
        return "home";
    }

    // Method to display recipes by selected category
    @GetMapping("/recipesByCategory")
    public String recipesByCategory(@RequestParam("categoryId") Long categoryId, Model model) {
        List<Recipe> recipes = recipeRepository.findByCategory_Id(categoryId);
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);  // To show the selected category in the dropdown
        return "home";
    }

    // Method to display a single recipe's details
    @GetMapping("/recipe/{id}")
    public String showRecipeDetails(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + id));
        
        model.addAttribute("recipe", recipe);
        return "recipeDetails";
    }

    // Login method
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            System.out.println("Login error occurred");
        }
        return "login";
    }

    // Method to show the form for adding a new recipe
    @GetMapping("/addRecipe")
    public String showAddRecipeForm(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("recipe", new Recipe());
        return "addRecipe";
    }

    // Method to handle the form submission for adding a new recipe
    @PostMapping("/addRecipe")
    public String addRecipe(Recipe recipe, 
                            @RequestParam List<String> ingredients, 
                            @RequestParam List<String> instructions, 
                            @AuthenticationPrincipal User currentUser) {
        
        // Associate the logged-in user with the recipe
        recipe.setIngredients(ingredients);
        recipe.setSteps(instructions);
        recipe.setUser(currentUser); // Associate the user with the recipe
        
        // Save the recipe to the repository
        recipeRepository.save(recipe);
        return "redirect:/profile"; // Redirect to profile page after saving
    }
}

