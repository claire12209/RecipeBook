package com.example.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/home")
    public String home(Model model) {
        List<Category> categories = recipeService.fetchAndStoreCategories();
        model.addAttribute("apiCategories", categories);
        return "home";
    }
}
