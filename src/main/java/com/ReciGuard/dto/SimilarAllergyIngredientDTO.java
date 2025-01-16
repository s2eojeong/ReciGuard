package com.ReciGuard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimilarAllergyIngredientDTO {

    @JsonProperty("ingredient")
    private List<String> similarIngredient;
}
