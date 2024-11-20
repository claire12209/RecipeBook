package com.example.springboot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrIngredientsContainingIgnoreCase(String name, String category, String ingredient);

    List<Recipe> findByCategory_Id(Long categoryId);

    // Add this method to fetch a recipe along with its reviews
    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.reviews WHERE r.id = :id")
    Recipe findByIdWithReviews(@Param("id") Long id);
}
