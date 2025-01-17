package com.ReciGuard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeListResponseDTO { //전체 리스트, cuisine 별 리스트, 검색 후 리스트
    private Long recipeId;
    private String imagePath;
    private String recipeName;
    private int serving;

    @JsonProperty("isScrapped")
    private boolean isScrapped;

    public RecipeListResponseDTO(Long recipeId, String recipeName, String imagePath, int serving, boolean isScrapped) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.imagePath = imagePath;
        this.serving = serving;
        this.isScrapped = isScrapped;
    }
}


