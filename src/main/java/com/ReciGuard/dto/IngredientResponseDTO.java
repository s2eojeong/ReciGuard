package com.ReciGuard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IngredientResponseDTO {

    private String ingredient; // 재료명
    private String quantity;

    public IngredientResponseDTO(String ingredient, String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}
