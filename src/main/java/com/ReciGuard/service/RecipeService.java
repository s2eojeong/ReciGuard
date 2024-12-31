package com.ReciGuard.service;

import com.ReciGuard.dto.*;
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

        if (recipe == null) {
            throw new IllegalArgumentException("레시피를 찾을 수 없습니다.");
        }

        RecipeStats stats = recipe.getRecipeStats();

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

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("레시피를 찾을 수 없습니다.");
        }

        return recipes.stream().map(recipe -> new RecipeListResponseDTO(
                                                    recipe.getRecipeName(),
                                                    recipe.getImagePath(),
                                                    recipe.getServing()))
                                             .collect(Collectors.toList());
    }

    // cuisine 별 레시피 리스트
    public List<RecipeListResponseDTO> getRecipesByCuisine(String cuisine){
        List<Recipe> recipes = recipeRepository.findByCuisine(cuisine);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("레시피를 찾을 수 없습니다.");
        }

        return recipes.stream().map(recipe -> new RecipeListResponseDTO(
                                                    recipe.getRecipeName(),
                                                    recipe.getImagePath(),
                                                    recipe.getServing()))
                                            .collect(Collectors.toList());
    }

    // query 검색에 따른 레시피 리스트
    public List<RecipeListResponseDTO> getRecipesByQuery(String query){
        List<Recipe> recipes = recipeRepository.findByQuery(query);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException(query + "로(으로) 검색된 결과가 없습니다.");
        }

        return recipes.stream().map(recipe -> new RecipeListResponseDTO(
                                                    recipe.getRecipeName(),
                                                    recipe.getImagePath(),
                                                    recipe.getServing()))
                                            .collect(Collectors.toList());
    }

    // 레시피 상세 검색
    public RecipeDetailResponseDTO getRecipeDetail(Long id) {

        // Recipe 엔티티 조회
        Recipe recipe = recipeRepository.findRecipeDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("요청한 데이터를 찾을 수 없습니다."));

        // Nutrition 정보 가져오기 (null 가능)
        Nutrition nutrition = recipe.getNutrition();

        // Ingredients 변환
        List<IngredientResponseDTO> ingredients = recipe.getRecipeIngredients().stream()
                .map(recipeIngredient -> new IngredientResponseDTO(
                        recipeIngredient.getIngredient().getIngredient(), // 재료명
                        recipeIngredient.getQuantity()                   // 수량
                ))
                .collect(Collectors.toList());

        // Instructions 변환
        List<InstructionResponseDTO> instructions = recipe.getInstructions().stream()
                .map(instruction -> new InstructionResponseDTO(
                        instruction.getInstructionImage(),
                        instruction.getInstruction()
                ))
                .collect(Collectors.toList());

        // RecipeDetailResponseDTO 생성 및 반환
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
