package com.ReciGuard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class IngredientRequestDTO {
    private String ingredient; // 재료명
    private String quantity;

    public IngredientRequestDTO(String ingredient, String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}
