package com.ReciGuard.dto;

import com.ReciGuard.entity.Recipe;
import com.ReciGuard.entity.RecipeStats;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeRecommendResponseDTO { // 오늘의 추천 레시피
    private String imagePath;
    private String recipeName;
    private int serving;
    private int scrapCount;
    private int viewCount;

    public RecipeRecommendResponseDTO(String imagePath, String recipeName,
                                      int serving, int scrapCount, int viewCount) {
        this.imagePath = imagePath;
        this.recipeName = recipeName;
        this.serving = serving;
        this.scrapCount = scrapCount;
        this.viewCount = viewCount;
    }
}
