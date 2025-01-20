package com.ReciGuard.controller;

import com.ReciGuard.dto.*;
import com.ReciGuard.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
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

    // 오늘의 추천 레시피
    @GetMapping("/recommend")
    public RecipeRecommendResponseDTO getTodayRecipe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        return recipeService.getTodayRecipe(userId);
    }

    // 전체 레시피 리스트
    @GetMapping("/all")
    public List<RecipeListResponseDTO> getRecipes(@RequestParam(required = false, defaultValue = "false") boolean filter) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);
        if (filter) {
            return recipeService.getAllFilteredRecipes(userId);
        }
        return recipeService.getAllRecipes(userId);
    }

    // cuisine별 레시피 리스트
    @GetMapping
    public List<RecipeListResponseDTO> getRecipesByCuisine(
            @RequestParam String cuisine,
            @RequestParam(required = false, defaultValue = "false") boolean filter) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);
        if (filter) {
            return recipeService.getFilteredRecipesByCuisine(userId, cuisine);
        }
        return recipeService.getRecipesByCuisine(userId, cuisine);
    }

    // query로 레시피 검색
    @GetMapping("/search")
    public List<RecipeListResponseDTO> getRecipesByQuery(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "false") boolean filter) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);
        if (filter) {
            return recipeService.getFilteredRecipesByQuery(userId, query);
        }
        return recipeService.getRecipesByQuery(userId, query);
    }

    // 레시피 상세 페이지
    @GetMapping("/{recipeId}")
    public RecipeDetailResponseDTO getRecipeDetail(@PathVariable Long recipeId) {
        log.info("Received recipeId: {}", recipeId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);
        recipeStatsService.increaseViewCount(recipeId); // viewCount 증가

        return recipeService.getRecipeDetail(recipeId, userId);
    }

    // 하트 버튼 눌러서 레시피 스크랩 (등록/수정)
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        return recipeService.findMyRecipes(userId);
    }

    // 나만의 레시피 저장
    @PostMapping("/myrecipe/save")
    public ResponseEntity<String> saveMyRecipe(
            @RequestPart("recipeForm") String recipeFormJson,
            @RequestPart(value = "recipeImage", required = false) MultipartFile recipeImage,
            @RequestPart(value = "instructionImageFiles", required = false) Map<String, MultipartFile> instructionImageFiles,
            HttpServletRequest request) {

        // instructionImageFiles 확인
        log.info("Received instructionImageFiles: {}", instructionImageFiles);

        // 로그 추가: 각 파일 정보 확인
        if (instructionImageFiles != null) {
            instructionImageFiles.forEach((key, file) -> log.info("Key: {}, File Name: {}", key, file.getOriginalFilename()));
        }

        // instructionImageFiles가 null일 경우 빈 Map으로 초기화
        if (instructionImageFiles == null) {
            instructionImageFiles = new HashMap<>();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        MyRecipeForm recipeForm;
        try {
            recipeForm = objectMapper.readValue(recipeFormJson, MyRecipeForm.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage());
        }
        log.info("Instruction Image Files: {}", instructionImageFiles);
        instructionImageFiles.forEach((key, file) -> log.info("Key: {}, File Name: {}", key, file.getOriginalFilename()));

        log.info("Received recipeForm: {}", recipeForm);
        log.info("Received instructionImageFiles: {}", instructionImageFiles);

        // Recipe 저장 서비스 호출
        recipeService.saveMyRecipe(recipeForm, recipeImage, instructionImageFiles, request);

        return ResponseEntity.ok("레시피가 성공적으로 등록되었습니다.");
    }

    // 나만의 레시피 수정 폼
    @GetMapping("/myrecipe/{recipeId}/edit")
    public MyRecipeForm UpdateMyRecipeForm(@PathVariable Long recipeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        return recipeService.getRecipeFormEdit(recipeId, userId);
    }

    // 나만의 레시피 수정
    @PostMapping(value = "/myrecipe/{recipeId}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMyRecipe(
            @PathVariable Long recipeId,
            @RequestPart("recipeForm") String recipeFormJson,
            @RequestPart(value = "recipeImage", required = false) MultipartFile recipeImage,
            @RequestPart(value = "instructionImageFiles", required = false) Map<String, MultipartFile> instructionImageFiles,
            HttpServletRequest request) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        try {
            // JSON 데이터를 객체로 변환
            MyRecipeFormEdit recipeForm = objectMapper.readValue(recipeFormJson, MyRecipeFormEdit.class);

            recipeService.updateMyRecipe(recipeId, recipeForm, recipeImage, instructionImageFiles, request);

            return ResponseEntity.ok("레시피가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("JSON 파싱 오류: " + e.getMessage());
        }
    }
}

