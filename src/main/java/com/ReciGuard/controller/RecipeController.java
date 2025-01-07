package com.ReciGuard.controller;

import com.ReciGuard.dto.RecipeDetailResponseDTO;
import com.ReciGuard.dto.RecipeListResponseDTO;
import com.ReciGuard.dto.RecipeRecommendResponseDTO;
import com.ReciGuard.service.RecipeService;
import com.ReciGuard.service.RecipeStatsService;
import com.ReciGuard.service.UserScrapService;
import com.ReciGuard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeStatsService recipeStatsService;
    private final UserScrapService userScrapService;
    private final UserService userService;

    // 오늘의 추천 레시피
    @GetMapping("/recommend")
    public RecipeRecommendResponseDTO getTodayRecipe(@RequestParam Long id){
        return recipeService.getTodayRecipe(id);
    }

    // 전체 레시피 리스트
    @GetMapping("/all")
    public List<RecipeListResponseDTO> getRecipes(@RequestParam(required = false, defaultValue = "false") boolean filter) {
        if (filter) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userService.findUserIdByUsername(username);
            return recipeService.getAllFilteredRecipes(userId);
        }
        return recipeService.getAllRecipes();
    }

    // cuisine별 레시피 리스트
    @GetMapping
    public List<RecipeListResponseDTO> getRecipesByCuisine(
            @RequestParam String cuisine,
            @RequestParam(required = false, defaultValue = "false") boolean filter) {
        if (filter) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userService.findUserIdByUsername(username);
            return recipeService.getFilteredRecipesByCuisine(userId, cuisine);
        }
        return recipeService.getRecipesByCuisine(cuisine);
    }

    // query로 레시피 검색
    @GetMapping("/search")
    public List<RecipeListResponseDTO> getRecipesByQuery(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "false") boolean filter) {
        if (filter) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userService.findUserIdByUsername(username);
            return recipeService.getFilteredRecipesByQuery(userId, query);
        }
        return recipeService.getRecipesByQuery(query);
    }

    // 레시피 상세 페이지
    @GetMapping("/{recipeId}")
    public RecipeDetailResponseDTO getRecipeDetail(@PathVariable Long id) {
        recipeStatsService.increaseViewCount(id); // viewCount 증가
        return recipeService.getRecipeDetail(id);
    }

    // 하트 버튼 눌러서 레시피 스크랩 (등록/수정)
    @PostMapping("/scrap/{recipeId}")
    public ResponseEntity<Boolean> toggleScrap(@PathVariable Long recipeId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        boolean isScrapped = userScrapService.toggleScrap(userId, recipeId);
        return ResponseEntity.ok(isScrapped);
    }
}
