package com.ReciGuard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MyRecipeForm {

    private Long id;

    private String recipeName;
    private int cookingTime;
    private String cuisine;
    private List<IngredientResponseDTO> ingredients;

}
