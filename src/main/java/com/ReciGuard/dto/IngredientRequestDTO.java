package com.ReciGuard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class IngredientRequestDTO {
    private String ingredient; // 재료명
    private String quantity;
}
