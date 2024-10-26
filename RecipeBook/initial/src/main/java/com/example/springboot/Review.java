package com.example.springboot;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // Rating from 1 to 5
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    // Default constructor
    public Review() {}

    // Constructor with fields
    public Review(int rating, String comment, User user, Recipe recipe) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.recipe = recipe;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) { // Validation for rating range
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", user=" + user +
                ", recipe=" + recipe +
                '}';
    }
}
