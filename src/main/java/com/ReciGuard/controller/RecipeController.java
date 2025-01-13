package com.ReciGuard.controller;

import com.ReciGuard.dto.*;
import com.ReciGuard.entity.Recipe;
import com.ReciGuard.service.RecipeService;
import com.ReciGuard.service.RecipeStatsService;
import com.ReciGuard.service.UserScrapService;
import com.ReciGuard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeStatsService recipeStatsService;
    private final UserScrapService userScrapService;
    private final UserService userService;
    private final RestTemplate restTemplate;

    @Value("${ai.model.api.url:https://example.com/recommend}") // 나중에 ai 모델 완성 후 수정
    private String aiModelApiUrl;

    // 오늘의 추천 레시피
    @GetMapping("/recommend")
    public RecipeRecommendResponseDTO getTodayRecipe(@RequestParam Long userId) {

        // 1. AI 모델에 전달할 데이터 준비
        Map<String, Object> requestPayload = recipeService.prepareAiModelInput(userId);

        // 2. AI 모델 API 호출
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                aiModelApiUrl,
                HttpMethod.POST,
                new HttpEntity<>(requestPayload),
                new ParameterizedTypeReference<Map<String, Object>>() {
        }
        );

        // 3. AI 모델의 추천 레시피 ID 추출
        if (response.getStatusCode().is2xxSuccessful()) {
            Long recipeId = Long.valueOf(response.getBody().get("recipe_id").toString());

            // 4. 추천 레시피 데이터 반환
            return recipeService.getTodayRecipe(recipeId);
        } else {
            throw new RuntimeException("AI 모델 API 호출 실패: " + response.getStatusCode());
        }
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
    public RecipeDetailResponseDTO getRecipeDetail(@PathVariable Long recipeId) {
        recipeStatsService.increaseViewCount(recipeId); // viewCount 증가
        return recipeService.getRecipeDetail(recipeId);
    }

//    // 하트 버튼 눌러서 레시피 스크랩 (등록/수정)
//    @PostMapping("/scrap/{recipeId}")
//    public ResponseEntity<Boolean> toggleScrap(@PathVariable Long recipeId){
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long userId = userService.findUserIdByUsername(username);
//
//        boolean isScrapped = userScrapService.toggleScrap(userId, recipeId);
//        return ResponseEntity.ok(isScrapped);
//    }
    @PostMapping("/scrap/{recipeId}")
    public ResponseEntity<String> toggleScrap(@PathVariable Long recipeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        boolean isScrapped = userScrapService.toggleScrap(userId, recipeId);
        String message = isScrapped
                ? "레시피가 성공적으로 스크랩되었습니다."
                : "레시피가 스크랩 취소 되었습니다.";

        return ResponseEntity.ok(message);
    }

    // 나만의 레시피 조회 (간단 리스트 조회)
    @GetMapping("/myrecipes")
    public List<RecipeListResponseDTO> getMyRecipes() {
        return recipeService.findMyRecipes();
    }

    // 나만의 레시피 저장
    @PostMapping("/myrecipe/save")
    public ResponseEntity<String> saveMyRecipe(@RequestBody MyRecipeForm recipeForm) {
        log.info("Received recipeForm: {}", recipeForm);
        recipeService.saveMyRecipe(recipeForm);

        return ResponseEntity.ok("레시피가 성공적으로 등록되었습니다.");
    }

    // 나만의 레시피 수정 폼
    @GetMapping("/myrecipe/{recipeId}/edit")
    public MyRecipeForm UpdateMyRecipeForm(@PathVariable Long recipeId) {
        return recipeService.getRecipeFormEdit(recipeId);
    }

    // 나만의 레시피 수정
    @PostMapping("/myrecipe/{recipeId}/edit")
    public RecipeDetailResponseDTO updateMyRecipe(
            @PathVariable Long recipeId,
            @RequestBody MyRecipeForm recipeForm) {
        return recipeService.updateMyRecipe(recipeId, recipeForm);
    }
}
