package com.ReciGuard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyRecipeForm {

    private String recipeName;
    private String imagePath;                        // 이미지 경로(메인 이미지)
    private Integer serving;                         // 인분 수
    private String cuisine;                          // 요리 종류 (예: 한식, 양식 등)
    private String foodType;                         // 음식 타입 (예: 밥, 면, 빵 등)
    private String cookingStyle;                     // 조리 방식 (예: 볶음, 찜 등)

    private List<IngredientResponseDTO> ingredients; // 재료 정보
    private List<InstructionResponseDTO> instructions; // 조리 단계 정보

}
