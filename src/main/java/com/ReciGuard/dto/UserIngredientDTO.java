package com.ReciGuard.dto;

import com.ReciGuard.entity.User;
import com.ReciGuard.entity.UserIngredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIngredientDTO {

    private Long id; // UserIngredient의 ID

    private Long userId; // User 엔티티의 ID

    private String ingredient; // Ingredient의 이름 (예: "계란", "새우")

    /**
     * 엔티티 -> DTO 변환 메서드
     */
    public static UserIngredientDTO fromEntity(UserIngredient userIngredient) {
        return UserIngredientDTO.builder()
                .id(userIngredient.getId())
                .userId(userIngredient.getUser().getUserid())
                .ingredient(userIngredient.getIngredient()) // Ingredient의 이름을 포함
                .build();
    }

    /**
     * DTO -> 엔티티 변환 메서드
     */
    public UserIngredient toEntity(User user) {
        UserIngredient userIngredient = new UserIngredient();
        userIngredient.setId(this.id);
        userIngredient.setUser(user);
        userIngredient.setIngredient(this.ingredient); // Ingredient의 이름 설정
        return userIngredient;
    }
}

