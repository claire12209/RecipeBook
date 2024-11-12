package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Search by recipe name or category name, case-insensitive
    List<Recipe> findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(String name, String category);

    // Find recipes by category ID
    List<Recipe> findByCategory_Id(Long categoryId);

    // Custom query to find recipes where ingredients contain the specified query, case-insensitive
    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    List<Recipe> findByIngredient(@Param("ingredient") String ingredient);
}
