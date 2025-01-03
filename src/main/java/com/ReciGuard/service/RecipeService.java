package com.ReciGuard.service;

import com.ReciGuard.dto.*;
import com.ReciGuard.entity.*;
import com.ReciGuard.repository.IngredientRepository;
import com.ReciGuard.repository.RecipeRepository;
import com.ReciGuard.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

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
    public List<RecipeListResponseDTO> getCuisineFilteredRecipes(Long userId, String cuisine) {
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
                        recipe.getServing()
                ))
                .collect(Collectors.toList());
    }

    // 레시피 상세 검색
    public RecipeDetailResponseDTO getRecipeDetail(Long id) {

        // 1. Recipe 엔티티 조회
        Recipe recipe = recipeRepository.findRecipeDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("요청한 데이터를 찾을 수 없습니다."));

        // 2. Nutrition 정보 가져오기 (null 가능)
        Nutrition nutrition = recipe.getNutrition();

        // 3. Ingredients 변환
        List<IngredientResponseDTO> ingredients = recipe.getRecipeIngredients().stream()
                .map(recipeIngredient -> new IngredientResponseDTO(
                        recipeIngredient.getIngredient().getIngredient(), // 재료명
                        recipeIngredient.getQuantity()                   // 수량
                ))
                .collect(Collectors.toList());

        // 4. Instructions 변환
        List<InstructionResponseDTO> instructions = recipe.getInstructions().stream()
                .map(instruction -> new InstructionResponseDTO(
                        instruction.getInstructionImage(),
                        instruction.getInstruction()
                ))
                .collect(Collectors.toList());

        // 5. RecipeDetailResponseDTO 생성 및 반환
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

    @Transactional
    public Recipe saveMyRecipe(MyRecipeForm recipeForm) {

        // 1. 현재 인증된 사용자의 username 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. username으로 User 엔티티 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 3. Recipe 엔티티 생성 및 설정
        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeForm.getRecipeName());
        recipe.setImagePath(recipeForm.getImagePath());
        recipe.setServing(recipeForm.getServing());
        recipe.setCuisine(recipeForm.getCuisine());
        recipe.setFoodType(recipeForm.getFoodType());
        recipe.setCookingStyle(recipeForm.getCookingStyle());

        // 4. user_id로 연관관계 설정
        recipe.setUser(user);

        // 5. Ingredients 저장
        List<RecipeIngredient> ingredients = recipeForm.getIngredients().stream()
                .map(ingredientDto -> {
                    RecipeIngredient recipeIngredient = new RecipeIngredient();

                    // Ingredient 조회
                    Ingredient ingredient = ingredientRepository.findByName(ingredientDto.getIngredient());

                    // RecipeIngredient 설정
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setQuantity(ingredientDto.getQuantity());
                    recipeIngredient.setRecipe(recipe);
                    return recipeIngredient;
                })
                .collect(Collectors.toList());
        recipe.setRecipeIngredients(ingredients);

        // 6. Instructions 저장
        List<Instruction> instructions = recipeForm.getInstructions().stream()
                .map(instructionDto -> {
                    Instruction instruction = new Instruction();
                    instruction.setInstructionImage(instructionDto.getInstructionImage());
                    instruction.setInstruction(instructionDto.getInstruction());
                    instruction.setRecipe(recipe);
                    return instruction;
                })
                .collect(Collectors.toList());
        recipe.setInstructions(instructions);

        // 7. RecipeStats 기본값 생성
        RecipeStats stats = new RecipeStats();
        stats.setScrapCount(0);
        stats.setViewCount(0);
        stats.setRecipe(recipe);
        recipe.setRecipeStats(stats);

        // 8. Recipe 저장
        return recipeRepository.save(recipe);
    }

    public List<RecipeListResponseDTO> findMyRecipes() {

        // 1. 현재 인증된 사용자의 username 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. username으로 User 엔티티 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 3. 해당 사용자가 등록한 레시피 조회 및 DTO로 변환
        return recipeRepository.findAllByUser(user).stream()
                .map(recipe -> new RecipeListResponseDTO(
                        recipe.getRecipeName(),
                        recipe.getImagePath(),
                        recipe.getServing()
                ))
                .collect(Collectors.toList());
    }

    public RecipeDetailResponseDTO updateMyRecipe(Long recipeId, MyRecipeForm recipeForm) {

        // 1. 레시피 ID로 영속 상태의 Recipe 엔티티 조회
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 2. 사용자 소유 확인
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        if (!recipe.getUser().equals(user)) {
            throw new SecurityException("자신이 등록한 레시피만 수정할 수 있습니다.");
        }

        // 3. 레시피 정보 수정
        recipe.setRecipeName(recipeForm.getRecipeName());
        recipe.setImagePath(recipeForm.getImagePath());
        recipe.setServing(recipeForm.getServing());
        recipe.setCuisine(recipeForm.getCuisine());
        recipe.setFoodType(recipeForm.getFoodType());
        recipe.setCookingStyle(recipeForm.getCookingStyle());

        // 4. 재료 수정
        if (recipeForm.getIngredients() != null) {
            List<RecipeIngredient> ingredients = recipeForm.getIngredients().stream()
                    .map(ingredientDto -> {
                        RecipeIngredient recipeIngredient = new RecipeIngredient();
                        Ingredient ingredient = ingredientRepository.findByName(ingredientDto.getIngredient());
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

                recipe.getRecipeIngredients().stream()
                        .map(ingredient -> new IngredientResponseDTO(
                                ingredient.getIngredient().getIngredient(),
                                ingredient.getQuantity()
                        ))
                        .collect(Collectors.toList()),

                recipe.getInstructions().stream()
                        .map(instruction -> new InstructionResponseDTO(
                                instruction.getInstruction(),
                                instruction.getInstructionImage()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
