package com.ReciGuard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IngredientResponseDTO {

    private String ingredient; // 재료명
    private String quantity;

    @JsonCreator
    public IngredientResponseDTO(@JsonProperty("ingredient") String ingredient,
                                 @JsonProperty("quantity") String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}
