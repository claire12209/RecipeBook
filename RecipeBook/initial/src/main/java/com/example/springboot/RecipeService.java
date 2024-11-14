package com.example.springboot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecipeService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CategoryRepository categoryRepository;

    // Fetch and parse categories from TheMealDB API
    public List<Category> fetchAndStoreCategories() {
        String url = "https://www.themealdb.com/api/json/v1/1/categories.php";
        String json = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode categoriesNode = root.path("categories");
            if (categoriesNode.isArray()) {
                for (JsonNode node : categoriesNode) {
                    String categoryName = node.path("strCategory").asText();
                    Category category = new Category(categoryName);
                    categoryRepository.save(category); // Save to database
                    categories.add(category);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
