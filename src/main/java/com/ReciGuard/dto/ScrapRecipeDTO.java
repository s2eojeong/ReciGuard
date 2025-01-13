package com.ReciGuard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapRecipeDTO {
    private Long recipeId;
    private String recipeName; // Recipe 엔티티에 있는 이름 필드라고 가정
    private LocalDateTime createdAt;
}
