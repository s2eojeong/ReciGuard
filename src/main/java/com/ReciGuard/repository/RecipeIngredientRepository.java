package com.ReciGuard.repository;

import com.ReciGuard.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    @Query("""
        SELECT ri
        FROM RecipeIngredient ri
        LEFT JOIN FETCH ri.ingredient ing
        WHERE ri.recipe.id = :recipeId
    """)
    List<RecipeIngredient> findRecipeIngredientsByRecipeId(@Param("recipeId") Long recipeId);
}
