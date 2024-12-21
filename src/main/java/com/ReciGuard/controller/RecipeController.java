package com.ReciGuard.controller;

import com.ReciGuard.dto.RecipeDetailResponseDTO;
import com.ReciGuard.dto.RecipeListResponseDTO;
import com.ReciGuard.dto.RecipeRecommendResponseDTO;
import com.ReciGuard.entity.Enum.Cuisine;
import com.ReciGuard.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    // 오늘의 추천 레시피
    @GetMapping
    public RecipeRecommendResponseDTO getTodayRecipe(Long id){
        return recipeService.getTodayRecipe(id);
    }

    // 모든 recipe 리스트
    @GetMapping("/all")
    public List<RecipeListResponseDTO> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    // cuisine별 레시피 리스트
    @GetMapping(params = "cuisine")
    public List<RecipeListResponseDTO> getRecipesByCuisine(@RequestParam Cuisine cuisine){
        return recipeService.getRecipesByCuisine(cuisine);
    }

    // Query 검색 결과 레시피 리스트
    @GetMapping("/search")
    public List<RecipeListResponseDTO> getRecipesByQuery(@RequestParam String query){
        return recipeService.getRecipesByQuery(query);
    }

    // 레시피 상세 페이지
    @GetMapping(params = "id")
    public RecipeDetailResponseDTO getRecipeDetail(@RequestParam Long id){
        return recipeService.getRecipeDetail(id);
    }
}
