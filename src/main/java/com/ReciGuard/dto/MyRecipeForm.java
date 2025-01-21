package com.ReciGuard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MyRecipeForm {

    private String recipeName;                       // 이미지 경로(메인 이미지)
    private int serving;                             // 인분 수
    private String cuisine;                          // 요리 종류 (예: 한식, 양식 등)
    private String foodType;                         // 음식 타입 (예: 밥, 면, 빵 등)
    private String cookingStyle;                     // 조리 방식 (예: 볶음, 찜 등)

    private List<IngredientResponseDTO> ingredients; // 재료 정보
    private List<InstructionResponseDTO> instructions; // 조리 단계 정보

}
