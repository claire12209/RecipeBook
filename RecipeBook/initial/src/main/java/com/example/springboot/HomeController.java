package com.example.springboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/home")
    public String home(Model model, @RequestParam(value = "query", required = false) String query) {
        List<Recipe> recipes;
    
        // Fetch recipes based on query
        if (query != null && !query.isEmpty()) {
            recipes = recipeRepository.findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(query, query);
        } else {
            recipes = recipeRepository.findAll();
        }
    
        // Fetch categories from both sources
        List<Category> categories = categoryRepository.findAll();
        categories.addAll(recipeService.fetchAndStoreCategories()); // Merge categories from RecipeService
    
        // Remove duplicates if needed (assuming Category has a proper equals and hashCode implementation)
        categories = categories.stream().distinct().toList();
    
        // Add attributes to the model
        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories); // Combined categories for the dropdown
    
        return "home";
    }
    

    // Method to handle searching by ingredient specifically
    @GetMapping("/search")
    public String searchRecipesByIngredient(@RequestParam("query") String query, Model model) {
        List<Recipe> recipes = recipeRepository.findByIngredient(query); // Using the custom query here
        
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        model.addAttribute("query", query);

        return "home";
    }

   // Method to display recipes by selected category or show all recipes if "All" is selected
    @GetMapping("/recipesByCategory")
    public String recipesByCategory(@RequestParam(value = "categoryId", required = false) Long categoryId, Model model) {
        List<Recipe> recipes;

        if (categoryId == null) {
            // If "All" is selected, retrieve all recipes
            recipes = recipeRepository.findAll();
        } else {
            // Otherwise, filter by the selected category ID
            recipes = recipeRepository.findByCategory_Id(categoryId);
        }

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
