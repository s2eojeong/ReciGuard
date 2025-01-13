package com.ReciGuard.repository;

import com.ReciGuard.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByIngredient(String ingredient);


}
