package com.example.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(String name, String category);

    List<Recipe> findByCategory_Id(Long categoryId);
}
