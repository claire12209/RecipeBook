package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springboot.RecipeRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {

    // Find all ratings for a specific recipe
    List<RecipeRating> findByRecipeId(Long recipeId);

    // Find all ratings for a specific user
    List<RecipeRating> findByUserId(Long userId);

    // Query to calculate the average rating for a specific recipe
    @Query("SELECT AVG(r.rating) FROM RecipeRating r WHERE r.recipe.id = :recipeId")
    Double findAverageRatingByRecipeId(@Param("recipeId") Long recipeId);
}
