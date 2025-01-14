package com.ReciGuard.service;

import com.ReciGuard.dto.*;
import com.ReciGuard.entity.*;
import com.ReciGuard.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UserService userService;
    private final RecipeStatsRepository recipeStatsRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.model.api.url:https://example.com/recommend}") // 나중에 ai 모델 완성 후 수정
    private String aiModelApiUrl;

    public RecipeRecommendResponseDTO getTodayRecipe(Long userId) {
        // AI 모델에 전달할 데이터 준비
        Map<String, Object> requestPayload = Map.of("userId", userId);

        try {
            // AI 모델 API 호출
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    aiModelApiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestPayload),
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                String imagePath = responseBody.get("imagePath").toString();
                String recipeName = responseBody.get("recipeName").toString();

                // 추천 레시피 반환
                return new RecipeRecommendResponseDTO(imagePath, recipeName);
            } else {
                throw new RuntimeException("AI 모델 API 호출 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("AI 모델 호출 중 오류 발생", e);
        }
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

    // 전체 레시피 리스트 -> 필터링 후
    public List<RecipeListResponseDTO> getAllFilteredRecipes(Long userId) {

        // 사용자 알레르기 정보를 제외한 레시피 필터링
        List<Recipe> recipes = recipeRepository.findAllFilteredRecipes(userId);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("조건에 맞는 레시피가 없습니다.");
        }

        // DTO 변환 및 반환
        return recipes.stream()
                .map(recipe -> new RecipeListResponseDTO(
                        recipe.getRecipeName(),
                        recipe.getImagePath(),
                        recipe.getServing()
                ))
                .collect(Collectors.toList());
    }

    // cuisine 별 레시피 리스트
    public List<RecipeListResponseDTO> getRecipesByCuisine(String cuisine){
        List<Recipe> recipes = recipeRepository.findByCuisine(cuisine);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("검색 결과가 없습니다.");
        }

        return recipes.stream().map(recipe -> new RecipeListResponseDTO(
                                                    recipe.getRecipeName(),
                                                    recipe.getImagePath(),
                                                    recipe.getServing()))
                                            .collect(Collectors.toList());
    }

    // cuisine 별 레시피 리스트 -> 필터링 후
    public List<RecipeListResponseDTO> getFilteredRecipesByCuisine(Long userId, String cuisine) {
        // 사용자 알레르기 정보를 제외한 특정 cuisine 레시피 필터링
        List<Recipe> recipes = recipeRepository.findCuisineFilteredRecipes(userId, cuisine);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException(cuisine + "에 해당하는 레시피가 없습니다.");
        }

        // DTO 변환 및 반환
        return recipes.stream()
                .map(recipe -> new RecipeListResponseDTO(
                        recipe.getRecipeName(),
                        recipe.getImagePath(),
                        recipe.getServing()
                ))
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

    // 검색 단어와 사용자 알레르기 정보를 기반으로 필터링된 레시피 리스트 검색
    public List<RecipeListResponseDTO> getFilteredRecipesByQuery(Long userId, String query) {
        // 사용자 알레르기 정보를 제외하고 검색 결과 필터링
        List<Recipe> recipes = recipeRepository.findQueryFilteredRecipes(userId, query);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException(query + "로 검색된 레시피가 없습니다.");
        }

        // DTO 변환 및 반환
        return recipes.stream()
                .map(recipe -> new RecipeListResponseDTO(
                        recipe.getRecipeName(),
                        recipe.getImagePath(),
                        recipe.getServing()))
                .collect(Collectors.toList());
    }

    // 레시피 상세 검색
    public RecipeDetailResponseDTO getRecipeDetail(Long id) {

        // 1. 기본 Recipe 정보 로드
        Recipe recipe = recipeRepository.findRecipeById(id)
                .orElseThrow(() -> new EntityNotFoundException("요청한 데이터를 찾을 수 없습니다."));

        // 2. RecipeStats 로드 (viewCount와 scrapCount)
        RecipeStats stats = recipeStatsRepository.findByRecipeId(id)
                .orElseThrow(() -> new EntityNotFoundException("RecipeStats 데이터를 찾을 수 없습니다."));

        // 2. Instructions와 RecipeIngredients 로드
        List<Instruction> instructions = recipeRepository.findInstructionsByRecipeId(id);
        List<RecipeIngredient> recipeIngredients = recipeRepository.findRecipeIngredientsByRecipeId(id);

        // 3. Nutrition 정보 가져오기 (null 가능)
        Nutrition nutrition = recipe.getNutrition();

        // 4. Ingredients 변환
        List<IngredientResponseDTO> ingredients = recipeIngredients.stream()
                .map(recipeIngredient -> new IngredientResponseDTO(
                        recipeIngredient.getIngredient().getIngredient(),
                        recipeIngredient.getQuantity()
                ))
                .collect(Collectors.toList());

        // 5. Instructions 변환
        List<InstructionResponseDTO> instructionDTOs = instructions.stream()
                .map(instruction -> new InstructionResponseDTO(
                        instruction.getInstructionImage(),
                        instruction.getInstruction()
                ))
                .collect(Collectors.toList());

        // 7. RecipeDetailResponseDTO 생성 및 반환
        return new RecipeDetailResponseDTO(
                recipe.getImagePath(),
                recipe.getRecipeName(),
                recipe.getServing(),
                recipe.getCuisine(),
                recipe.getFoodType(),
                recipe.getCookingStyle(),
                nutrition != null ? (int) nutrition.getCalories() : 0,
                nutrition != null ? (int) nutrition.getSodium() : 0,
                nutrition != null ? (int) nutrition.getCarbohydrate() : 0,
                nutrition != null ? (int) nutrition.getFat() : 0,
                nutrition != null ? (int) nutrition.getProtein() : 0,
                stats.getScrapCount(),
                stats.getViewCount(),
                ingredients,
                instructionDTOs
        );
    }

    @Transactional
    public Recipe saveMyRecipe(MyRecipeForm recipeForm) {

        // 1. 현재 인증된 사용자의 username 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        // 2. Recipe 엔티티 생성 및 설정
        Recipe recipe = new Recipe();

        recipe.setRecipeName(recipeForm.getRecipeName());
        recipe.setImagePath(recipeForm.getImagePath());
        recipe.setServing(recipeForm.getServing());
        recipe.setCuisine(recipeForm.getCuisine());
        recipe.setFoodType(recipeForm.getFoodType());
        recipe.setCookingStyle(recipeForm.getCookingStyle());

        // 3. userId로 User 엔티티 연관관계 설정
        User user = new User();
        user.setUserid(userId); // ID만 설정하여 연관관계 매핑
        recipe.setUser(user);

        // 4. Ingredients 저장
        List<RecipeIngredient> ingredients = recipeForm.getIngredients().stream()
                .map(ingredientDto -> {
                    RecipeIngredient recipeIngredient = new RecipeIngredient();

                    // Ingredient 조회
                    Ingredient ingredient = ingredientRepository.findByIngredient(ingredientDto.getIngredient());

                    // RecipeIngredient 설정
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setQuantity(ingredientDto.getQuantity());
                    recipeIngredient.setRecipe(recipe);
                    return recipeIngredient;
                })
                .collect(Collectors.toList());
        recipe.setRecipeIngredients(ingredients);

        // 5. Instructions 저장
        AtomicInteger counter = new AtomicInteger(1); // 1부터 시작하는 카운터 생성
        List<Instruction> instructions = recipeForm.getInstructions().stream()
                .map(instructionDto -> {
                    Instruction instruction = new Instruction();
                    instruction.setInstructionImage(instructionDto.getInstructionImage());
                    instruction.setInstruction(instructionDto.getInstruction());
                    instruction.setInstructionId(counter.getAndIncrement()); // 순차적으로 instruction_id 설정
                    instruction.setRecipe(recipe);
                    return instruction;
                })
                .collect(Collectors.toList());
        recipe.setInstructions(instructions);


        // 6. RecipeStats 기본값 생성
        RecipeStats stats = new RecipeStats();
        stats.setScrapCount(0);
        stats.setViewCount(0);
        stats.setRecipe(recipe);
        recipe.setRecipeStats(stats);

        // 7. Recipe 저장
        return recipeRepository.save(recipe);
    }

    public List<RecipeListResponseDTO> findMyRecipes() { // 리스트로 반환 (간단 조회)

        // 1. 현재 인증된 사용자의 username 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. username으로 userId 조회
        Long userId = userService.findUserIdByUsername(username);

        // 3. 해당 사용자의 ID로 등록한 레시피 조회 및 DTO로 변환
        return recipeRepository.findAllByUserId(userId).stream()
                .map(recipe -> new RecipeListResponseDTO(
                        recipe.getRecipeName(),
                        recipe.getImagePath(),
                        recipe.getServing()
                ))
                .collect(Collectors.toList());
    }

    // 수정 폼 데이터 반환
    public MyRecipeForm getRecipeFormEdit(Long recipeId) {
        // 1. 현재 인증된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        // 2. recipeId로 레시피 조회 (소유권 검증 포함)
        Recipe recipe = recipeRepository.findRecipeByUserId(recipeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        // 3. MyRecipeForm 생성 및 데이터 매핑
        MyRecipeForm form = new MyRecipeForm();
        form.setRecipeName(recipe.getRecipeName());
        form.setImagePath(recipe.getImagePath());
        form.setServing(recipe.getServing());
        form.setCuisine(recipe.getCuisine());
        form.setFoodType(recipe.getFoodType());
        form.setCookingStyle(recipe.getCookingStyle());

        // 4. 재료 정보 매핑
        List<IngredientResponseDTO> ingredientDTOs = recipe.getRecipeIngredients().stream()
                .map(ingredient -> new IngredientResponseDTO(
                        ingredient.getIngredient().getIngredient(),
                        ingredient.getQuantity()
                ))
                .collect(Collectors.toList());
        form.setIngredients(ingredientDTOs);

        // 5. 조리 단계 정보 매핑
        List<InstructionResponseDTO> instructionDTOs = recipe.getInstructions().stream()
                .map(instruction -> new InstructionResponseDTO(
                        instruction.getInstruction(),
                        instruction.getInstructionImage()
                ))
                .collect(Collectors.toList());
        form.setInstructions(instructionDTOs);

        return form;
    }

    @Transactional
    public RecipeDetailResponseDTO updateMyRecipe(Long recipeId, MyRecipeForm recipeForm) {

        // 1. 레시피 ID로 영속 상태의 Recipe 엔티티 조회
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 2. 현재 인증된 사용자의 userId 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        // 4. 레시피 정보 수정
        recipe.setRecipeName(recipeForm.getRecipeName());
        recipe.setImagePath(recipeForm.getImagePath());
        recipe.setServing(recipeForm.getServing());
        recipe.setCuisine(recipeForm.getCuisine());
        recipe.setFoodType(recipeForm.getFoodType());
        recipe.setCookingStyle(recipeForm.getCookingStyle());

        // 5. 재료 수정
        if (recipeForm.getIngredients() != null) {
            List<RecipeIngredient> existingIngredients = recipe.getRecipeIngredients();

            // 업데이트 또는 삭제를 위한 매핑
            Map<String, String> formIngredientMap = recipeForm.getIngredients().stream()
                    .filter(ingredientDto -> {
                        // 재료명과 수량 둘 다 공백인 경우 삭제로 간주 (필터 제외)
                        if (ingredientDto.getIngredient().isBlank() && ingredientDto.getQuantity().isBlank()) {
                            return false; // 제외
                        }
                        // 재료명이나 수량 둘 중 하나라도 공백인 경우 예외 발생
                        if (ingredientDto.getIngredient().isBlank() || ingredientDto.getQuantity().isBlank()) {
                            throw new IllegalArgumentException("재료와 수량을 확인해주세요.");
                        }
                        return true; // 유지
                    })
                    .collect(Collectors.toMap(
                            IngredientResponseDTO::getIngredient,
                            IngredientResponseDTO::getQuantity
                    ));

            // 삭제할 재료를 명시적으로 관리
            List<RecipeIngredient> ingredientsToRemove = new ArrayList<>();
            for (RecipeIngredient existingIngredient : existingIngredients) {
                String formQuantity = formIngredientMap.get(existingIngredient.getIngredient().getIngredient());

                if (formQuantity == null) {
                    // 폼에 없는 데이터는 삭제 대상에 추가
                    ingredientsToRemove.add(existingIngredient);
                } else if (!existingIngredient.getQuantity().equals(formQuantity)) {
                    // quantity가 다르면 업데이트
                    existingIngredient.setQuantity(formQuantity);
                }
            }

            // 1-1. 삭제 대상 제거 (Hibernate에게 명확히 알림)
            ingredientsToRemove.forEach(existingIngredients::remove);
            recipeIngredientRepository.deleteAll(ingredientsToRemove);

            // 2. 추가 처리: 폼에만 있는 데이터 추가
            recipeForm.getIngredients().forEach(ingredientDto -> {
                boolean isExisting = existingIngredients.stream()
                        .anyMatch(existing -> existing.getIngredient().getIngredient().equals(ingredientDto.getIngredient()));

                if (!isExisting) {
                    // 새로 추가
                    RecipeIngredient newIngredient = new RecipeIngredient();
                    Ingredient ingredient = ingredientRepository.findByIngredient(ingredientDto.getIngredient());
                    newIngredient.setIngredient(ingredient);
                    newIngredient.setQuantity(ingredientDto.getQuantity());
                    newIngredient.setRecipe(recipe);
                    existingIngredients.add(newIngredient);
                }
            });
        }

        // 6. 조리 과정 수정
        if (recipeForm.getInstructions() != null) {
            List<Instruction> existingInstructions = recipe.getInstructions();

            // 기존 데이터 매핑 (instruction_id 기준으로)
            Map<Integer, Instruction> existingInstructionMap = existingInstructions.stream()
                    .collect(Collectors.toMap(
                            Instruction::getInstructionId, // key: instruction_id
                            instruction -> instruction // value: Instruction 객체
                    ));

            // 업데이트 리스트
            List<Instruction> updatedInstructions = new ArrayList<>();

            // 입력된 순서대로 처리
            AtomicInteger currentInstructionId = new AtomicInteger(1); // 순서 관리
            recipeForm.getInstructions().forEach(instructionDto -> {
                Instruction existingInstruction = existingInstructionMap.get(currentInstructionId.get());

                if (existingInstruction != null) {
                    // 기존 데이터 수정
                    existingInstruction.setInstruction(instructionDto.getInstruction());
                    existingInstruction.setInstructionImage(instructionDto.getInstructionImage());
                    updatedInstructions.add(existingInstruction);
                } else {
                    // 새 데이터 추가
                    Instruction newInstruction = new Instruction();
                    newInstruction.setInstructionId(currentInstructionId.get());
                    newInstruction.setInstruction(instructionDto.getInstruction());
                    newInstruction.setInstructionImage(instructionDto.getInstructionImage());
                    newInstruction.setRecipe(recipe);
                    updatedInstructions.add(newInstruction);
                }
                currentInstructionId.incrementAndGet();
            });

            // 기존 데이터 중 폼에 없는 데이터는 삭제 처리
            existingInstructions.removeIf(existing ->
                    updatedInstructions.stream().noneMatch(updated -> updated.getInstructionId() == existing.getInstructionId())
            );

            // 기존 리스트를 업데이트된 리스트로 교체
            existingInstructions.clear();
            existingInstructions.addAll(updatedInstructions);
        }

        // 7. 수정된 레시피를 RecipeDetailResponseDTO로 변환 & 리턴
        return new RecipeDetailResponseDTO(
                recipe.getImagePath(),
                recipe.getRecipeName(),
                recipe.getServing(),
                recipe.getCuisine(),
                recipe.getFoodType(),
                recipe.getCookingStyle(),

                // 사용자가 작성한 레시피에 대해서는 nutrition 정보 제공 x -> 다 0으로 기본 세팅
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getCalories() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getSodium() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getCarbohydrate() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getFat() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getProtein() : 0,

                recipe.getRecipeStats().getViewCount(), // 조회 수
                recipe.getRecipeStats().getScrapCount(), // 스크랩 수

                recipe.getRecipeIngredients() != null ? recipe.getRecipeIngredients().stream()
                        .map(ingredient -> new IngredientResponseDTO(
                                ingredient.getIngredient().getIngredient(),
                                ingredient.getQuantity()
                        ))
                        .collect(Collectors.toList()) : Collections.emptyList(),
                recipe.getInstructions() != null ? recipe.getInstructions().stream()
                        .map(instruction -> new InstructionResponseDTO(
                                instruction.getInstruction(),
                                instruction.getInstructionImage()
                        ))
                        .collect(Collectors.toList()) : Collections.emptyList()
        );
    }
}
