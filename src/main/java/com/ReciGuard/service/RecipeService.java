package com.ReciGuard.service;

import com.ReciGuard.dto.RecipeDetailResponseDTO;
import com.ReciGuard.dto.RecipeListResponseDTO;
import com.ReciGuard.dto.RecipeRecommendResponseDTO;
import com.ReciGuard.entity.Nutrition;
import com.ReciGuard.entity.Recipe;
import com.ReciGuard.entity.RecipeStats;
import com.ReciGuard.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    // 오늘의 레시피 추천
    public RecipeRecommendResponseDTO getTodayRecipe(Long id){
        Recipe recipe = recipeRepository.findTodayRecipe(id);

        RecipeStats stats = recipe.getRecipeStats().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Recipe stats not found"));

        return new RecipeRecommendResponseDTO(
                recipe.getImagePath(),
                recipe.getRecipeName(),
                recipe.getServing(),
                stats.getScrapCount(),
                stats.getViewCount()
        );
    }

    // 전체 레시피 리스트
    public List<RecipeListResponseDTO> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(recipe -> new RecipeListResponseDTO(
                                                    recipe.getRecipeName(),
                                                    recipe.getImagePath(),
                                                    recipe.getServing()))
                                             .collect(Collectors.toList());
    }

    // cuisine 별 레시피 리스트
    public List<RecipeListResponseDTO> getRecipesByCuisine(String cuisine){
        List<Recipe> recipes = recipeRepository.findByCuisine(cuisine);
        return recipes.stream().map(recipe -> new RecipeListResponseDTO(
                                                    recipe.getRecipeName(),
                                                    recipe.getImagePath(),
                                                    recipe.getServing()))
                                            .collect(Collectors.toList());
    }

    // query 검색에 따른 레시피 리스트
    public List<RecipeListResponseDTO> getRecipesByQuery(String query){
        List<Recipe> recipes = recipeRepository.findByQuery(query);
        return recipes.stream().map(recipe -> new RecipeListResponseDTO(
                                                    recipe.getRecipeName(),
                                                    recipe.getImagePath(),
                                                    recipe.getServing()))
                                            .collect(Collectors.toList());
    }

    // 레시피 상세 검색
    public RecipeDetailResponseDTO getRecipeDetail(Long id) {

        Recipe recipe = recipeRepository.findRecipeDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + id));

        List<Map<String, String>> ingredients = recipe.getRecipeIngredients().stream()
                .map(ri -> Map.of(
                        "ingredient", ri.getIngredient().getIngredient(),
                        "quantity", ri.getQuantity()))
                .collect(Collectors.toList());

        List<Map<String, String>> instructions = recipe.getInstructions().stream()
                .map(instruction -> Map.of(
                        "instructionImage", instruction.getInstructionImage(),
                        "instruction", instruction.getInstruction()))
                .collect(Collectors.toList());

        Nutrition nutrition = recipe.getNutrition();

        return new RecipeDetailResponseDTO(
                recipe.getImagePath(),
                recipe.getRecipeName(),
                recipe.getServing(),
                recipe.getCuisine(),
                recipe.getFoodType(),
                recipe.getCookingStyle(),
                nutrition != null ? nutrition.getCalories() : 0, // null일 경우 기본값 0으로 되게끔
                nutrition != null ? nutrition.getSodium() : 0,
                nutrition != null ? nutrition.getCarbohydrate() : 0,
                nutrition != null ? nutrition.getFat() : 0,
                nutrition != null ? nutrition.getProtein() : 0,
                ingredients,
                instructions);
    }
}
