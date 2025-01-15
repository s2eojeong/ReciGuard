package com.ReciGuard.dto;

import com.ReciGuard.entity.Recipe;
import com.ReciGuard.entity.RecipeStats;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeRecommendResponseDTO { // 오늘의 추천 레시피
    private Long recipeId;
    private String imagePath;
    private String recipeName;

    public RecipeRecommendResponseDTO(Long recipeId, String imagePath, String recipeName) {
        this.recipeId = recipeId;
        this.imagePath = imagePath;
        this.recipeName = recipeName;
    }
}
