package com.ReciGuard.service;

import com.ReciGuard.dto.*;
import com.ReciGuard.entity.*;
import com.ReciGuard.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeStatsRepository recipeStatsRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final UserScrapRepository userScrapRepository;
    private final InstructionRepository instructionRepository;
    private final UserIngredientRepository userIngredientRepository;
    private final S3Uploader s3Uploader;
    private final RestTemplate restTemplate;
    private final NutritionRepository nutritionRepository;

    @Value("http://15.164.219.9:8000/recommend")
    private String recipeRecommendApiUrl;

    @Value("http://54.180.85.44:8000/check_allergy")
    private String similarAllergyApiUrl;

    public RecipeRecommendResponseDTO getTodayRecipe(Long userId) {
        // AI 모델에 전달할 데이터 준비
        Map<String, Object> requestPayload = Map.of("user_id", userId);

        try {
            // AI 모델 API 호출
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    recipeRecommendApiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestPayload),
                    new ParameterizedTypeReference<>() {
                    }
            );

            log.info("AI 모델 응답: {}", response.getBody());
            log.info("Calling AI model with userId: {}", userId);

            Map<String, Object> responseBody = response.getBody();
            if (!responseBody.containsKey("recipe_id")) {
                log.error("AI 모델 응답에 recipeId가 없습니다.");
                return new RecipeRecommendResponseDTO(null, null, null);
            }

            // AI 모델로부터 받은 recipeId
            Double recipeIdDouble = Double.valueOf(responseBody.get("recipe_id").toString());
            Long recipeId = recipeIdDouble.longValue();

            // recipeId에 해당하는 레시피 정보 조회
            Recipe recipe = recipeRepository.findById(recipeId)
                        .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다: " + recipeId));

            // 추천 레시피 반환
            return new RecipeRecommendResponseDTO(
                                        recipe.getId(),
                                        recipe.getImagePath(),
                                        recipe.getRecipeName());

            } catch (HttpClientErrorException e){
                log.error("AI 모델 API 호출 실패: {}", e.getStatusCode());
                log.info("Calling AI model with userId: {}", userId);
                return new RecipeRecommendResponseDTO(null, null, null);
            } catch (Exception e) {
                log.error("AI 모델 호출 중 오류 발생: {}", e.getMessage());
                log.info("Calling AI model with userId: {}", userId);
                return new RecipeRecommendResponseDTO(null, null, null);
        }
    }

    // 전체 레시피 리스트
    public List<RecipeListResponseDTO> getAllRecipes(Long userId) {

        List<Recipe> recipes = recipeRepository.findAll();

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("레시피를 찾을 수 없습니다.");
        }

        return recipes.stream()
                .map(recipe -> {
                    boolean isScrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());
                    return new RecipeListResponseDTO(
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getImagePath(),
                            recipe.getServing(),
                            isScrapped // 추가
                    );
                })
                .collect(Collectors.toList());
    }

    // 전체 레시피 리스트 -> 필터링 후
    public List<RecipeListResponseDTO> getAllFilteredRecipes(Long userId) {

        // 사용자 알레르기 재료명 목록 가져오기
        List<String> allergyIngredient = userIngredientRepository.findAllergyIngredientsByUserId(userId);

        String allergyIngredients = allergyIngredient.stream()
                .map(String::trim)
                .collect(Collectors.joining(" ")); // "계란 우유" 형태로 변환

        // 필터링된 레시피 가져오기
        List<Recipe> recipes = recipeRepository.findAllFilteredRecipes(userId, allergyIngredients);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("조건에 맞는 레시피가 없습니다.");
        }

        // DTO 변환 및 반환
        return recipes.stream()
                .map(recipe -> {
                    boolean scrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());
                    return new RecipeListResponseDTO(
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getImagePath(),
                            recipe.getServing(),
                            scrapped
                    );
                })
                .collect(Collectors.toList());
    }

    // cuisine 별 레시피 리스트
    public List<RecipeListResponseDTO> getRecipesByCuisine(Long userId, String cuisine){
        List<Recipe> recipes = recipeRepository.findByCuisine(cuisine);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("검색 결과가 없습니다.");
        }

        return recipes.stream()
                .map(recipe -> {
                    boolean scrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());
                    return new RecipeListResponseDTO(
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getImagePath(),
                            recipe.getServing(),
                            scrapped
                    );
                })
                .collect(Collectors.toList());
    }

    // cuisine 별 레시피 리스트 -> 필터링 후
    public List<RecipeListResponseDTO> getFilteredRecipesByCuisine(Long userId, String cuisine) {

        // 사용자 알레르기 정보를 제외한 레시피 필터링
        List<Recipe> recipes = recipeRepository.findCuisineFilteredRecipes(userId, cuisine);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("사용자 알레르기 정보를 바탕으로 " + cuisine + "에 해당하는 레시피가 없습니다.");
        }

        // DTO 변환 및 반환
        return recipes.stream()
                .map(recipe -> {
                    boolean scrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());
                    return new RecipeListResponseDTO(
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getImagePath(),
                            recipe.getServing(),
                            scrapped
                    );
                })
                .collect(Collectors.toList());
    }

    // query 검색에 따른 레시피 리스트
    public List<RecipeListResponseDTO> getRecipesByQuery(Long userId, String query){
        List<Recipe> recipes = recipeRepository.findByQuery(query);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException(query + "로(으로) 검색된 결과가 없습니다.");
        }

        return recipes.stream()
                .map(recipe -> {
                    boolean scrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());
                    return new RecipeListResponseDTO(
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getImagePath(),
                            recipe.getServing(),
                            scrapped
                    );
                })
                .collect(Collectors.toList());
    }

    // 검색 단어와 사용자 알레르기 정보를 기반으로 필터링된 레시피 리스트 검색
    public List<RecipeListResponseDTO> getFilteredRecipesByQuery(Long userId, String query) {
        // 사용자 알레르기 정보를 제외한 레시피 필터링
        List<Recipe> recipes = recipeRepository.findQueryFilteredRecipes(userId, query);

        if (recipes.isEmpty()) {
            throw new EntityNotFoundException("사용자 알레르기 정보를 바탕으로 " + query + "로 검색된 레시피가 없습니다.");
        }

        // DTO 변환 및 반환
        return recipes.stream()
                .map(recipe -> {
                    boolean scrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());
                    return new RecipeListResponseDTO(
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getImagePath(),
                            recipe.getServing(),
                            scrapped
                    );
                })
                .collect(Collectors.toList());
    }

    // 알레르기 유발 가능한 유사 재료 받아오는 ai 모델
    private SimilarAllergyIngredientDTO getSimilarAllergyIngredients(Long recipeId, Long userId) {
        try {
            // 요청 페이로드 생성
            Map<String, Object> requestPayload = Map.of(
                    "recipe_id", recipeId,
                    "user_id", userId
            );

            // AI 모델 API 호출
            ResponseEntity<SimilarAllergyIngredientDTO> response = restTemplate.exchange(
                    similarAllergyApiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestPayload),
                    SimilarAllergyIngredientDTO.class
            );
            log.info("AI 모델 응답: {}", response.getBody());
            log.info("Calling AI model with recipeId: {} and userId: {}", recipeId, userId);

            // 응답 본문 반환 (null 체크)
            response.getBody();
            return response.getBody();
        } catch (Exception e) {
            log.error("AI 모델 호출 실패: {}", e.getMessage());
            log.info("Calling AI model with recipeId: {} and userId: {}", recipeId, userId);

            return new SimilarAllergyIngredientDTO(Collections.emptyList());
        }
    }

    // 레시피 상세 검색
    public RecipeDetailResponseDTO getRecipeDetail(Long recipeId, Long userId) {

        long startTime = System.nanoTime();
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("요청한 데이터를 찾을 수 없습니다."));
        long endTime = System.nanoTime();
        long executionTimeMs = (endTime - startTime) / 1_000_000; // ns → ms 변환
        log.info("쿼리 실행 시간: {} ms", executionTimeMs);

        // 6. Ingredients 변환
        List<IngredientResponseDTO> ingredients = recipe.getRecipeIngredients().stream()
                .map(recipeIngredient -> new IngredientResponseDTO(
                        recipeIngredient.getIngredient().getIngredient(),
                        recipeIngredient.getQuantity()
                ))
                .collect(Collectors.toList());

        // 7. Instructions 변환
        List<InstructionResponseDTO> instructionDTOs = recipe.getInstructions().stream()
                .map(instruction -> new InstructionResponseDTO(
                        instruction.getInstructionId(),
                        instruction.getInstructionImage(),
                        instruction.getInstruction()
                ))
                .collect(Collectors.toList());

        // 8. AI 모델에서 유사 알레르기 유발 재료 가져오기
        SimilarAllergyIngredientDTO similarAllergyIngredientsDTO = getSimilarAllergyIngredients(recipe.getId(), userId);
        List<String> similarAllergyIngredients = similarAllergyIngredientsDTO.getSimilarIngredient();

        // isScrapped 값 확인
        boolean scrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());

        // 9. RecipeDetailResponseDTO 생성 및 반환
        return new RecipeDetailResponseDTO(
                recipe.getImagePath(),
                recipe.getRecipeName(),
                recipe.getUser() != null ? recipe.getUser().getUserId() : null,
                recipe.getServing(),
                recipe.getCuisine(),
                recipe.getFoodType(),
                recipe.getCookingStyle(),
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getCalories() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getSodium() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getCarbohydrate() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getFat() : 0,
                recipe.getNutrition() != null ? (int) recipe.getNutrition().getProtein() : 0,
                scrapped,
                recipe.getRecipeStats() != null ? recipe.getRecipeStats().getScrapCount() : 0,
                recipe.getRecipeStats() != null ? recipe.getRecipeStats().getViewCount() : 0,
                ingredients,
                instructionDTOs,
                similarAllergyIngredients
        );
    }

    @Transactional
    public void saveMyRecipe(MyRecipeForm recipeForm, MultipartFile recipeImage, Map<String, MultipartFile> instructionImageFiles, HttpServletRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username:" + username));

        String uploadedRecipeImage = null;
        if (recipeImage != null && !recipeImage.isEmpty()) {
            uploadedRecipeImage = s3Uploader.saveFile(recipeImage);
            log.info("Received recipeImage: {}, size: {}", recipeImage.getOriginalFilename(), recipeImage.getSize());
        } else {
            log.info("No recipeImage provided.");
        }

        Recipe recipe = Recipe.builder()
                .user(user)
                .recipeName(recipeForm.getRecipeName())
                .imagePath(uploadedRecipeImage)
                .serving(recipeForm.getServing())
                .cuisine(recipeForm.getCuisine())
                .foodType(recipeForm.getFoodType())
                .cookingStyle(recipeForm.getCookingStyle())
                .build();
        recipeRepository.save(recipe);

        //Ingredients 저장
        final Recipe finalRecipe = recipe;
        List<RecipeIngredient> ingredients = recipeForm.getIngredients().stream()
                .map(ingredientDto -> {
                    // Ingredient 조회 또는 생성
                    Ingredient ingredient = ingredientRepository.findByIngredient(ingredientDto.getIngredient());
                    if (ingredient == null) {
                        Ingredient newIngredient = Ingredient.builder()
                                .ingredient(ingredientDto.getIngredient())
                                .build();
                        ingredient = ingredientRepository.save(newIngredient);
                    }
                    // RecipeIngredient 생성
                    return RecipeIngredient.builder()
                            .ingredient(ingredient)
                            .quantity(ingredientDto.getQuantity())
                            .recipe(finalRecipe)
                            .build();
                })
                .toList();

        //Instructions 저장
        if (request instanceof MultipartHttpServletRequest multipartRequest) {
            Map<String, MultipartFile> parsedInstructionFiles = new HashMap<>();
            multipartRequest.getFileMap().forEach((key, value) -> {
                if (key.startsWith("instructionImageFiles[")) {
                    parsedInstructionFiles.put(key, value);
                }
            });
            instructionImageFiles = parsedInstructionFiles;
        }
        // 파싱된 파일 키 확인
        log.info("Parsed file keys: {}", instructionImageFiles != null ? instructionImageFiles.keySet() : "No files provided");

        // recipe_id 범위 내에서 instruction_id 시작값 설정
        AtomicInteger instructionCounter = new AtomicInteger(
                instructionRepository.findMaxInstructionIdByRecipeId(recipe.getId()).orElse(0) + 1
        );

        Map<String, MultipartFile> finalInstructionImageFiles = instructionImageFiles;
        List<Instruction> instructions = recipeForm.getInstructions().stream()
                .map(instructionDto -> {
                    int currentInstructionId = instructionCounter.getAndIncrement(); // instruction_id 설정
                    // 현재 처리 중인 Instruction ID 확인
                    log.info("Processing instruction ID: {}", currentInstructionId);

                    String matchingKey = finalInstructionImageFiles != null
                            ? finalInstructionImageFiles.keySet().stream()
                            .filter(key -> key.equals("instructionImageFiles[" + currentInstructionId + "]"))
                            .findFirst()
                            .orElse(null)
                            : null;

                    String uploadedUrl = null;
                    if (matchingKey != null) {
                        log.info("Found matching key for instruction {}: {}", currentInstructionId, matchingKey);
                        MultipartFile file = finalInstructionImageFiles.get(matchingKey);

                        // 파일이 있으면 업로드
                        if (file != null && !file.isEmpty()) {
                            try {
                                uploadedUrl = s3Uploader.saveFile(file);
                                log.info("Uploaded image for instruction {}: {}", currentInstructionId, uploadedUrl);
                            } catch (Exception e) {
                                log.error("Failed to upload image for instruction {}: {}", currentInstructionId, e.getMessage());
                                uploadedUrl = null; // 업로드 실패 시 null 처리
                            }
                        } else {
                            log.info("No file provided for instruction {}", currentInstructionId);
                        }
                    } else {
                        log.info("No matching file found for instruction {}", currentInstructionId);
                    }

                    return Instruction.builder()
                            .instructionId(currentInstructionId)
                            .instruction(instructionDto.getInstruction())
                            .instructionImage(uploadedUrl)
                            .recipe(finalRecipe)
                            .build();
                })
                .toList();

        Nutrition nutrition = Nutrition.builder()
                .fat(0)
                .calories(0)
                .carbohydrate(0)
                .protein(0)
                .sodium(0)
                .recipe(recipe)
                .build();

        RecipeStats stats = RecipeStats.builder()
                .scrapCount(0)
                .viewCount(0)
                .recipe(recipe)
                .build();

        recipeIngredientRepository.saveAll(ingredients);
        instructionRepository.saveAll(instructions);
        nutritionRepository.save(nutrition);
        recipeStatsRepository.save(stats);

        recipeRepository.save(recipe);
    }

    public List<RecipeListResponseDTO> findMyRecipes(Long userId) { // 리스트로 반환 (간단 조회)
        // 해당 사용자의 ID로 등록한 레시피 조회 및 DTO로 변환
        return recipeRepository.findAllByUser_UserId(userId).stream()
                .map(recipe -> {
                    boolean scrapped = userScrapRepository.existsUserScrap(userId, recipe.getId());
                    return new RecipeListResponseDTO(
                            recipe.getId(),
                            recipe.getRecipeName(),
                            recipe.getImagePath(),
                            recipe.getServing(),
                            scrapped
                    );
                })
                .collect(Collectors.toList());
    }

    // 수정 폼 데이터 반환
    public MyRecipeForm getRecipeFormEdit(Long recipeId, Long userId) {
        // 2. recipeId로 레시피 조회 (소유권 검증 포함)
        Recipe recipe = recipeRepository.findRecipeByUserId(recipeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        // 4. 재료 정보 매핑
        List<IngredientResponseDTO> ingredientDTOs = recipe.getRecipeIngredients().stream()
                .map(ingredient -> new IngredientResponseDTO(
                        ingredient.getIngredient().getIngredient(),
                        ingredient.getQuantity()
                ))
                .collect(Collectors.toList());

        // 5. 조리 단계 정보 매핑
        List<InstructionResponseDTO> instructionDTOs = recipe.getInstructions().stream()
                .map(instruction -> new InstructionResponseDTO(
                        instruction.getInstructionId(),
                        instruction.getInstructionImage(),
                        instruction.getInstruction()
                        ))
                .collect(Collectors.toList());

        return MyRecipeForm.builder()
                .recipeName(recipe.getRecipeName())
                .imagePath(recipe.getImagePath())
                .serving(recipe.getServing())
                .cuisine(recipe.getCuisine())
                .foodType(recipe.getFoodType())
                .cookingStyle(recipe.getCookingStyle())
                .ingredients(ingredientDTOs)
                .instructions(instructionDTOs)
                .build();
    }

    @Transactional
    public void updateMyRecipe(Long recipeId, MyRecipeFormEdit recipeForm, MultipartFile recipeImage,
                               Map<String, MultipartFile> instructionImageFiles, HttpServletRequest request) {

        // 1. 레시피 ID로 영속 상태의 Recipe 엔티티 조회
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 2. 레시피 메인 이미지 처리
        String uploadedImagePath = null;
        if (recipeForm.isImageRemoved() && recipe.getImagePath() != null) {
            s3Uploader.deleteFile(recipe.getImagePath()); // 기존 이미지 삭제
        } else if (recipeImage != null && !recipeImage.isEmpty()) {
            try {
                // 새 이미지와 기존 이미지 비교 후 업데이트
                String newImageHash = s3Uploader.calculateFileHash(recipeImage);
                String existingImageHash = recipe.getImagePath() != null ? calculateFileHashFromS3(recipe.getImagePath()) : null;

                if (!newImageHash.equals(existingImageHash)) {
                    if (recipe.getImagePath() != null) {
                        s3Uploader.deleteFile(recipe.getImagePath()); // 기존 이미지 삭제
                    }
                    uploadedImagePath = s3Uploader.saveFile(recipeImage);
                }
            } catch (Exception e) {
                throw new RuntimeException("update failed : ", e);
            }
        }

        // 3. 레시피 정보 수정
        recipe.modifyMyRecipe(
                uploadedImagePath,
                recipe.getServing(),
                recipeForm.getRecipeName(),
                recipeForm.getCuisine(),
                recipeForm.getFoodType(),
                recipeForm.getCookingStyle()
        );

        // 4. 재료 수정
        if (recipeForm.getIngredients() != null) {
            List<RecipeIngredient> existingIngredients = recipe.getRecipeIngredients();

            // 업데이트 또는 삭제를 위한 매핑
            Map<String, String> formIngredientMap = recipeForm.getIngredients().stream()
                    .filter(ingredientDto -> {
                        if (ingredientDto.getIngredient().isBlank() && ingredientDto.getQuantity().isBlank()) {
                            return false; // 제외
                        }
                        if (ingredientDto.getIngredient().isBlank() || ingredientDto.getQuantity().isBlank()) {
                            throw new IllegalArgumentException("재료와 수량을 확인해주세요.");
                        }
                        return true; // 유지
                    })
                    .collect(Collectors.toMap(
                            IngredientRequestDTO::getIngredient,
                            IngredientRequestDTO::getQuantity
                    ));

            // 삭제할 재료 관리
            List<RecipeIngredient> ingredientsToRemove = new ArrayList<>();
            for (RecipeIngredient existingIngredient : existingIngredients) {
                String formQuantity = formIngredientMap.get(existingIngredient.getIngredient().getIngredient());

                if (formQuantity == null) {
                    ingredientsToRemove.add(existingIngredient);
                } else if (!existingIngredient.getQuantity().equals(formQuantity)) {
                    existingIngredient.setQuantity(formQuantity); // 수량 업데이트
                }
            }

            // 삭제 대상 제거
            ingredientsToRemove.forEach(existingIngredients::remove);
            recipeIngredientRepository.deleteAll(ingredientsToRemove);

            // 추가 처리
            recipeForm.getIngredients().forEach(ingredientDto -> {
                boolean isExisting = existingIngredients.stream()
                        .anyMatch(existing -> existing.getIngredient().getIngredient().equals(ingredientDto.getIngredient()));

                if (!isExisting) {
                    Ingredient ingredient = ingredientRepository.findByIngredient(ingredientDto.getIngredient());

                    if (ingredient == null) {
                        ingredient = new Ingredient();
                        ingredient.setIngredient(ingredientDto.getIngredient());
                        ingredient = ingredientRepository.save(ingredient);
                    }

                    RecipeIngredient newIngredient = new RecipeIngredient();
                    newIngredient.setIngredient(ingredient);
                    newIngredient.setQuantity(ingredientDto.getQuantity());
                    newIngredient.setRecipe(recipe);
                    existingIngredients.add(newIngredient);
                }
            });
        }

        // 5. 조리 과정 수정
        if (request instanceof MultipartHttpServletRequest multipartRequest) {
            Map<String, MultipartFile> parsedInstructionFiles = new HashMap<>();
            multipartRequest.getFileMap().forEach((key, value) -> {
                if (key.startsWith("instructionImageFiles[")) {
                    parsedInstructionFiles.put(key, value);
                }
            });
            instructionImageFiles = parsedInstructionFiles;
        }
        if (recipeForm.getInstructions() != null) {
            List<Instruction> existingInstructions = recipe.getInstructions();

            AtomicInteger instructionCounter = new AtomicInteger(
                    existingInstructions.stream()
                            .mapToInt(Instruction::getInstructionId)
                            .max()
                            .orElse(0) + 1
            );

            Map<Integer, Instruction> existingInstructionMap = existingInstructions.stream()
                    .collect(Collectors.toMap(
                            Instruction::getInstructionId,
                            instruction -> instruction
                    ));

            List<Instruction> updatedInstructions = new ArrayList<>();

            Map<String, MultipartFile> finalInstructionImageFiles = instructionImageFiles;
            recipeForm.getInstructions().forEach(instructionDto -> {
                if (instructionDto.getInstructionId() == null) {
                    instructionDto.setInstructionId(instructionCounter.getAndIncrement());
                }

                Instruction instruction = existingInstructionMap.get(instructionDto.getInstructionId());
                if (instruction == null) {
                    instruction = new Instruction();
                    instruction.setRecipe(recipe);
                    instruction.setInstructionId(instructionDto.getInstructionId());
                }

                instruction.setInstruction(instructionDto.getInstruction());

                String key = "instructionImageFiles[" + instructionDto.getInstructionId() + "]";
                if (finalInstructionImageFiles != null && finalInstructionImageFiles.containsKey(key)) {
                    handleInstructionImage(instruction, finalInstructionImageFiles.get(key));
                } else if (instructionDto.isImageRemoved()) {
                    handleInstructionImage(instruction, null);
                }

                updatedInstructions.add(instruction);
            });

            existingInstructions.removeIf(existing ->
                    updatedInstructions.stream().noneMatch(updated -> updated.getInstructionId().equals(existing.getInstructionId()))
            );

            existingInstructions.clear();
            existingInstructions.addAll(updatedInstructions);
        }

        // 6. 수정된 레시피를 RecipeDetailResponseDTO로 변환 & 리턴
        if (recipe.getRecipeIngredients() != null) {
            recipe.getRecipeIngredients().stream()
                    .map(ingredient -> new IngredientResponseDTO(
                            ingredient.getIngredient().getIngredient(),
                            ingredient.getQuantity()
                    ))
                    .collect(Collectors.toList());
        }
        if (recipe.getInstructions() != null) {
            recipe.getInstructions().stream()
                    .map(instruction -> new InstructionResponseDTO(
                            instruction.getInstructionId(),
                            instruction.getInstructionImage(),
                            instruction.getInstruction()
                    ))
                    .collect(Collectors.toList());
        }
    }

    // 이미지 처리 메서드
    private void handleInstructionImage(Instruction instruction, MultipartFile newImageFile) {
        try {
            if (newImageFile != null && !newImageFile.isEmpty()) {
                // 새 이미지의 해시값 계산
                String newImageHash = s3Uploader.calculateFileHash(newImageFile);

                // 기존 이미지가 있는 경우 기존 이미지의 해시값 계산
                String existingImageHash = null;
                if (instruction.getInstructionImage() != null) {
                    existingImageHash = calculateFileHashFromS3(instruction.getInstructionImage());
                }

                if (!newImageHash.equals(existingImageHash)) {
                    log.info("New image is different from existing image. Proceeding with upload.");

                    // 기존 이미지 삭제
                    if (instruction.getInstructionImage() != null) {
                        log.info("Deleting existing image from S3: {}", instruction.getInstructionImage());
                        s3Uploader.deleteFile(instruction.getInstructionImage());
                    }

                    // 새 이미지 업로드 및 경로 저장
                    String uploadedImageUrl = s3Uploader.saveFile(newImageFile);
                    instruction.setInstructionImage(uploadedImageUrl);
                    log.info("Uploaded new image to S3: {}", uploadedImageUrl);
                } else {
                    log.info("New image is identical to existing image. No changes made.");
                }
            } else if (instruction.getInstructionImage() != null) {
                // 이미지를 삭제하는 경우
                log.info("Deleting existing image from S3 for instructionId {}: {}", instruction.getInstructionId(), instruction.getInstructionImage());
                s3Uploader.deleteFile(instruction.getInstructionImage());
                instruction.setInstructionImage(null);
            }
        } catch (Exception e) {
            log.error("Failed to handle instruction image: {}", e.getMessage());
            throw new RuntimeException("Failed to handle instruction image", e);
        }
    }

    private String calculateFileHashFromS3(String s3FilePath) throws NoSuchAlgorithmException {
        byte[] fileBytes = s3Uploader.downloadFile(s3FilePath); // S3에서 파일 다운로드
        if (fileBytes == null) {
            return null;
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fileBytes);
        return Base64.getEncoder().encodeToString(hash); // 해시값을 Base64로 인코딩
    }

    @Transactional
    public void deleteMyRecipe(Long recipeId){
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레시피입니다."));

        recipeRepository.delete(recipe);
    }
}
