package com.example.springboot;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeRatingRepository recipeRatingRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(
        Model model,
        @RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "category", required = false) String category
    ) {
        List<Recipe> recipes;
    
        if (query != null && !query.isEmpty()) {
            recipes = recipeRepository.findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrIngredientsContainingIgnoreCase(query, query, query);
        } else if (category != null && !category.equalsIgnoreCase("ALL")) {
            Long categoryId = categoryRepository.findByName(category).getId(); // Get ID from category name
            recipes = recipeRepository.findByCategory_Id(categoryId);
        } else {
            recipes = recipeRepository.findAll(); // Fetch all recipes
        }
    
        // Calculate and set the average rating for each recipe
        for (Recipe recipe : recipes) {
            Double average = recipeRatingRepository.findAverageRatingByRecipeId(recipe.getId());
            recipe.setAverageRating(average != null ? average : 0.0);
        }
    
        // Fetch all categories
        List<Category> categories = categoryRepository.findAll();
    
        // Add "ALL" as a string at the top of the categories
        model.addAttribute("categories", categories);
        model.addAttribute("allOption", "ALL");
        model.addAttribute("recipes", recipes);
        return "home";
    }
    
     
    

    @GetMapping("/recipesByCategory")
    public String recipesByCategory(@RequestParam("categoryId") Long categoryId, Model model) {
        List<Recipe> recipes = recipeRepository.findByCategory_Id(categoryId);

        // Calculate and set the average rating for each recipe in the selected category
        for (Recipe recipe : recipes) {
            Double average = recipeRatingRepository.findAverageRatingByRecipeId(recipe.getId());
            recipe.setAverageRating(average != null ? average : 0.0);
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        return "home";
    }

    @GetMapping("/recipe/{id}")
    public String showRecipeDetails(@PathVariable Long id, Model model) {
        // Fetch the recipe by ID
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + id));
    
        // Force initialization of the reviews list
        recipe.getReviews().size(); // This will trigger the lazy-loading of reviews,Ensure reviews are loaded

        // Format the review dates
        for (Review review : recipe.getReviews()) {
            if (review.getDate() != null) {
                // Format the date as a string and set it in the review object
                review.setFormattedDate(review.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        }
    
        // Calculate and set the average rating
        Double average = recipeRatingRepository.findAverageRatingByRecipeId(recipe.getId());
        recipe.setAverageRating(average != null ? average : 0.0);
    
        // Add recipe details to the model
        model.addAttribute("recipe", recipe);
    
        // Add reviews to the model
        model.addAttribute("reviews", recipe.getReviews());
    
        return "recipeDetails";
    }
    
    

    @PostMapping("/recipe/{id}/rate")
    public String rateRecipe(@PathVariable Long id, @RequestParam int rating, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            // Fetch recipe and logged-in user
            Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
            String username = authentication.getName(); // Get logged-in username
            User user = userService.findByUsername(username); // Fetch the User object
            
            if (user == null) {
                throw new RuntimeException("User not authenticated");
            }
    
            // Save the rating
            RecipeRating recipeRating = new RecipeRating(recipe, user, rating);
            recipeRatingRepository.save(recipeRating);
    
            // Recalculate and save the average rating
            Double average = recipeRatingRepository.findAverageRatingByRecipeId(recipe.getId());
            recipe.setAverageRating(average != null ? average : 0.0);
            recipeRepository.save(recipe);
    
            return "redirect:/recipe/" + id;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while saving your rating.");
            return "redirect:/recipe/" + id;
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            System.out.println("Login error occurred");
        }
        return "login";
    }

    @GetMapping("/addRecipe")
    public String showAddRecipeForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("recipe", new Recipe());
        return "addRecipe";
    }

    @PostMapping("/addRecipe")
    public String addRecipe(Recipe recipe, 
                            @RequestParam List<String> ingredients, 
                            @RequestParam List<String> instructions, 
                            @AuthenticationPrincipal User currentUser) {
        recipe.setIngredients(ingredients);
        recipe.setSteps(instructions);
        recipe.setUser(currentUser); // Associate the user with the recipe
        recipeRepository.save(recipe);
        return "redirect:/profile";
    }
}