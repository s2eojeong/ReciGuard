package com.ReciGuard.dto;

import com.ReciGuard.entity.Enum.CookingStyle;
import com.ReciGuard.entity.Enum.Cuisine;
import com.ReciGuard.entity.Enum.FoodType;
import com.ReciGuard.entity.Ingredient;
import com.ReciGuard.entity.Instruction;

import java.util.List;

public class RecipeDetailResponseDTO {
    private String imagePath;
    private String recipeName;
    private int serving;

    private Cuisine cuisine;
    private FoodType foodType;
    private CookingStyle cookingStyle;

    private int calories;
    private int sodium;
    private int carbohydrate;
    private int fat;
    private int protein;

    private List<Ingredient> ingredients;

    private List<Instruction> instructions;
}
