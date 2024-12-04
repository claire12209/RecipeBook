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
        
        // Calculate and set the average rating for each recipe, including review ratings
        for (Recipe recipe : recipes) {
            // Fetch recipe ratings
            List<RecipeRating> recipeRatings = recipeRatingRepository.findByRecipeId(recipe.getId());
            
            // Fetch review ratings (if reviews exist)
            List<Review> reviews = recipe.getReviews();
            
            // Calculate the total ratings from both recipe ratings and review ratings
            double totalRating = 0;
            int totalCount = 0;
            
            // Add recipe ratings to total
            for (RecipeRating recipeRating : recipeRatings) {
                totalRating += recipeRating.getRating();
                totalCount++;
            }
            
            // Add review ratings to total
            for (Review review : reviews) {
                totalRating += review.getRating();
                totalCount++;
            }
            
            // Calculate the average rating
            Double average = (totalCount > 0) ? totalRating / totalCount : 0.0;
            
            // Set the average rating on the recipe
            recipe.setAverageRating(average);
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
    public String showRecipeDetails(
        @PathVariable Long id,
        @RequestParam(value = "filter", defaultValue = "all") String filter, // Add filter parameter
        Model model
    ) {
        // Fetch the recipe by ID
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + id));
    
        // Force initialization of the reviews list
        recipe.getReviews().size(); // Ensure reviews are loaded
    
        // Calculate and set the average rating including both recipe ratings and review ratings
        Double average = calculateAverageRating(recipe);
        recipe.setAverageRating(average != null ? average : 0.0);
    
        // Filter reviews based on the query parameter
        List<Review> reviews;
        if ("recent".equalsIgnoreCase(filter)) {
            reviews = recipe.getReviews().stream()
                            .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate())) // Sort by date descending
                            .limit(5) // Limit to the most recent 5 reviews (adjust as needed)
                            .toList();
        } else {
            reviews = recipe.getReviews(); // Show all reviews
        }
    
        // Format the review dates for Thymeleaf
        reviews.forEach(review -> {
            if (review.getDate() != null) {
                review.setFormattedDate(review.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        });
    
        // Add recipe details to the model
        model.addAttribute("recipe", recipe);
    
        // Add reviews to the model
        model.addAttribute("reviews", reviews);
    
        // Add filtered reviews to the model
        model.addAttribute("filter", filter);
    
        return "recipeDetails";
    }
    
    private Double calculateAverageRating(Recipe recipe) {
        // Fetch all recipe ratings
        List<RecipeRating> recipeRatings = recipeRatingRepository.findByRecipeId(recipe.getId());
        // Fetch all review ratings
        List<Review> reviews = recipe.getReviews();
    
        // Calculate the total ratings (RecipeRating + Review rating)
        double totalRating = 0;
        int totalCount = 0;
    
        // Add recipe ratings to total
        for (RecipeRating recipeRating : recipeRatings) {
            totalRating += recipeRating.getRating();
            totalCount++;
        }
    
        // Add review ratings to total
        for (Review review : reviews) {
            totalRating += review.getRating();
            totalCount++;
        }
    
        // Return the average if there are any ratings, otherwise return null (no ratings)
        if (totalCount > 0) {
            return totalRating / totalCount;
        } else {
            return null;
        }
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