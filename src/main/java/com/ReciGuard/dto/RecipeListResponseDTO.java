package com.ReciGuard.dto;

import com.ReciGuard.entity.Recipe;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeListResponseDTO { //전체 리스트, cuisine 별 리스트, 검색 후 리스트
    private String imagePath;
    private String recipeName;
    private int serving;

    public RecipeListResponseDTO(String recipeName, String imagePath, int serving) {
        this.recipeName = recipeName;
        this.imagePath = imagePath;
        this.serving = serving;
    }
}


