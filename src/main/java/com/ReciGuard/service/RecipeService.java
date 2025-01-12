package com.ReciGuard.service;

import com.ReciGuard.dto.*;
import com.ReciGuard.entity.*;
import com.ReciGuard.repository.IngredientRepository;
import com.ReciGuard.repository.RecipeRepository;
import com.ReciGuard.repository.RecipeStatsRepository;
import com.ReciGuard.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final UserRepository userRepository;

    // ai 모델에게 넘기는 데이터
    public Map<String, Object> prepareAiModelInput(Long userId) {
        // 1. 사용자 정보 로드
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 모든 레시피 로드
        List<Recipe> recipes = recipeRepository.findAll();

        // 3. 사용자가 스크랩한 데이터 로드
        List<UserScrap> userScraps = userRepository.findScrapsByUserId(userId);

        // 4. 레시피 통계 데이터 로드
        List<RecipeStats> recipeStats = recipeStatsRepository.findAll();

        // 5. AI 모델 입력 데이터 구성
        Map<String, Object> inputData = new HashMap<>();

        // 사용자 선호도
        inputData.put("User_Cuisine", user.getCuisines());
        inputData.put("User_FoodType", user.getFoodTypes());
        inputData.put("User_CookingStyle", user.getCookingStyles());

        // 레시피 데이터
        inputData.put("Recipe", recipes.stream()
                .map(recipe -> Map.of(
                        "cuisine", recipe.getCuisine() != null ? recipe.getCuisine() : "NULL",
                        "food_type", recipe.getFoodType() != null ? recipe.getFoodType() : "NULL",
                        "cooking_style", recipe.getCookingStyle() != null ? recipe.getCookingStyle() : "NULL"
                ))
                .collect(Collectors.toList()));

        // 사용자 스크랩 데이터
        inputData.put("User_Scrap", userScraps.stream()
                .map(scrap -> Map.of(
                        "recipe_id", scrap.getRecipe().getId(),
                        "created_at", scrap.getCreatedAt()
                ))
                .collect(Collectors.toList()));

        // 레시피 통계 데이터
        inputData.put("Recipe_Stats", recipeStats.stream()
                .map(stats -> Map.of(
                        "view_count", stats.getViewCount(),
                        "scrap_count", stats.getScrapCount()
                ))
                .collect(Collectors.toList()));

        return inputData;
    }

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
                nutrition != null ? nutrition.getCalories() : 0,
                nutrition != null ? nutrition.getSodium() : 0,
                nutrition != null ? nutrition.getCarbohydrate() : 0,
                nutrition != null ? nutrition.getFat() : 0,
                nutrition != null ? nutrition.getProtein() : 0,
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

        // 3. 사용자 소유 확인
        if (!recipe.getUser().getUserid().equals(userId)) {
            throw new SecurityException("자신이 등록한 레시피만 수정할 수 있습니다.");
        }

        // 4. 레시피 정보 수정
        recipe.setRecipeName(recipeForm.getRecipeName());
        recipe.setImagePath(recipeForm.getImagePath());
        recipe.setServing(recipeForm.getServing());
        recipe.setCuisine(recipeForm.getCuisine());
        recipe.setFoodType(recipeForm.getFoodType());
        recipe.setCookingStyle(recipeForm.getCookingStyle());

        // 5. 재료 수정
        if (recipeForm.getIngredients() != null) {
            List<RecipeIngredient> ingredients = recipeForm.getIngredients().stream()
                    .map(ingredientDto -> {
                        RecipeIngredient recipeIngredient = new RecipeIngredient();
                        Ingredient ingredient = ingredientRepository.findByIngredient(ingredientDto.getIngredient());
                        recipeIngredient.setIngredient(ingredient);
                        recipeIngredient.setQuantity(ingredientDto.getQuantity());
                        recipeIngredient.setRecipe(recipe);
                        return recipeIngredient;
                    })
                    .collect(Collectors.toList());
            recipe.setRecipeIngredients(ingredients);
        }

        // 5. 조리 과정 수정
        if (recipeForm.getInstructions() != null) {
            List<Instruction> instructions = recipeForm.getInstructions().stream()
                    .map(instructionDto -> {
                        Instruction instruction = new Instruction();
                        instruction.setInstruction(instructionDto.getInstruction());
                        instruction.setInstructionImage(instructionDto.getInstructionImage());
                        instruction.setRecipe(recipe);
                        return instruction;
                    })
                    .collect(Collectors.toList());
            recipe.setInstructions(instructions);
        }

        // 6. 수정된 레시피를 RecipeDetailResponseDTO로 변환 & 리턴
        return new RecipeDetailResponseDTO(
                recipe.getImagePath(),
                recipe.getRecipeName(),
                recipe.getServing(),
                recipe.getCuisine(),
                recipe.getFoodType(),
                recipe.getCookingStyle(),

                recipe.getNutrition() != null ? recipe.getNutrition().getCalories() : 0,
                recipe.getNutrition() != null ? recipe.getNutrition().getSodium() : 0,
                recipe.getNutrition() != null ? recipe.getNutrition().getCarbohydrate() : 0,
                recipe.getNutrition() != null ? recipe.getNutrition().getFat() : 0,
                recipe.getNutrition() != null ? recipe.getNutrition().getProtein() : 0,

                recipe.getRecipeStats().getViewCount(), // 조회 수 (변경하지 않음)
                recipe.getRecipeStats().getScrapCount(), // 스크랩 수 (변경하지 않음)

                recipe.getRecipeIngredients() != null ? recipe.getRecipeIngredients().stream()
                        .map(ingredient -> new IngredientResponseDTO(
                                ingredient.getIngredient().getIngredient(), // String 타입의 재료 이름 반환
                                ingredient.getQuantity() // String 타입의 수량 반환
                        ))
                        .collect(Collectors.toList()) : Collections.emptyList(),

                recipe.getInstructions() != null ? recipe.getInstructions().stream()
                        .map(instruction -> new InstructionResponseDTO(
                                instruction.getInstruction(), // String 타입의 조리 단계
                                instruction.getInstructionImage() // String 타입의 이미지 경로
                        ))
                        .collect(Collectors.toList()) : Collections.emptyList()
        );
    }
}
