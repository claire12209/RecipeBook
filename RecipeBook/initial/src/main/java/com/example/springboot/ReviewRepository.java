package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//interface manage interactions with Review entity, allow us to retrieve and manage review in the database
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Custom query to find all reviews for a specific recipe
    List<Review> findByRecipeId(Long recipeId);

    // Custom query to find all reviews by a specific user
    List<Review> findByUserId(Long userId);
}
