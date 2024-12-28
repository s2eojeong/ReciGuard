package com.ReciGuard.dto;

import com.ReciGuard.entity.Ingredient;
import com.ReciGuard.entity.Instruction;

import java.util.List;
import java.util.Map;

public class RecipeDetailResponseDTO {
    private String imagePath;
    private String recipeName;
    private int serving;

    private String cuisine;
    private String foodType;
    private String cookingStyle;

    private int calories;
    private int sodium;
    private int carbohydrate;
    private int fat;
    private int protein;

    private List<Map<String, String>> ingredients;
    private List<Map<String, String>> instructions;

    public RecipeDetailResponseDTO(String imagePath,
                                   String recipeName,
                                   int serving,
                                   String cuisine,
                                   String foodType,
                                   String cookingStyle,
                                   int calories, int sodium, int carbohydrate, int fat, int protein,
                                   List<Map<String, String>> ingredients,
                                   List<Map<String, String>> instructions) {
        this.imagePath = imagePath;
        this.recipeName = recipeName;
        this.serving = serving;
        this.cuisine = cuisine;
        this.foodType = foodType;
        this.cookingStyle = cookingStyle;
        this.calories = calories;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
}
