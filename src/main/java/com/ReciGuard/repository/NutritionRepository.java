package com.ReciGuard.repository;

import com.ReciGuard.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {

    Optional<Nutrition> findByRecipe_Id(Long recipeId);
}
