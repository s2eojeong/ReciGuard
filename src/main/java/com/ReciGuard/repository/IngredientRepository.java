package com.ReciGuard.repository;

import com.ReciGuard.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByIngredient(String ingredient);

    @Query("SELECT i FROM Ingredient i WHERE i.ingredient = :ingredient")
    Optional<Ingredient> findFirstByIngredient(@Param("ingredient") String ingredient);

}
