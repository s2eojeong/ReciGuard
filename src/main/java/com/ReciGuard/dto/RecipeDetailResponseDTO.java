package com.ReciGuard.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class RecipeDetailResponseDTO {
    private String imagePath;
    private String recipeName;
    private Long userId;
    private int serving;

    private String cuisine;
    private String foodType;
    private String cookingStyle;

    private int calories;
    private int sodium;
    private int carbohydrate;
    private int fat;
    private int protein;

    private boolean scrapped;
    private int scrapCount;
    private int viewCount;

    private List<IngredientResponseDTO> ingredients; // 재료 리스트
    private List<InstructionResponseDTO> instructions; // 조리 과정 리스트
    private List<String> similarAllergyIngredients; // 유사 알레르기 유발 재료

    public RecipeDetailResponseDTO(String imagePath,
                                   String recipeName,
                                   Long userId,
                                   int serving,
                                   String cuisine,
                                   String foodType,
                                   String cookingStyle,
                                   int calories, int sodium, int carbohydrate, int fat, int protein,
                                   boolean scrapped, int scrapCount, int viewCount,
                                   List<IngredientResponseDTO> ingredients,
                                   List<InstructionResponseDTO> instructions,
                                   List<String> similarAllergyIngredients) {
        this.imagePath = imagePath;
        this.recipeName = recipeName;
        this.userId = userId;
        this.serving = serving;
        this.cuisine = cuisine;
        this.foodType = foodType;
        this.cookingStyle = cookingStyle;
        this.calories = calories;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.scrapped = scrapped;
        this.scrapCount = scrapCount;
        this.viewCount = viewCount;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.similarAllergyIngredients = similarAllergyIngredients;
    }

}
