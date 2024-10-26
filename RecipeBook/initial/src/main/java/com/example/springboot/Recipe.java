package com.example.springboot;

import jakarta.persistence.*;
import java.util.List;
//store information about recipes, user enter in name, description, ingredients, categories.
//user can select multiple categories for their recipe
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000) // Allows for a longer description
    private String description;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;

    @ManyToMany
    @JoinTable(
        name = "recipe_categories",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    // Default constructor
    public Recipe() {}

    // Constructor with fields
    public Recipe(String name, String description, List<String> ingredients, List<Category> categories) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.categories = categories;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", categories=" + categories +
                '}';
    }
}
