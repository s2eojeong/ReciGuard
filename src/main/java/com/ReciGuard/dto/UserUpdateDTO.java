package com.ReciGuard.dto;

import com.ReciGuard.entity.Ingredient;
import com.ReciGuard.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserUpdateDTO {

    /**
     * 사용자 요청 데이터를 처리하기 위한 DTO 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Long id;

        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{4,20}$", message = "아이디는 특수문자를 제외한 4~20자리여야 합니다.")
        private String username;

        private String gender; // String으로 유지하거나 Enum으로 변경 가능

        private Integer age;

        private Double weight;

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotNull(message = "cookingStyle cannot be null")
        private List<String> userCookingStyle;

        @NotNull(message = "cuisine cannot be null")
        private List<String> userCuisine;

        @NotNull(message = "Food type cannot be null")
        private List<String> userFoodType;

        @JsonProperty("ingredients")
        private List<String> ingredients;

        /* DTO -> Entity */
        public User toEntity() {
            User user = User.builder()
                    .userid(id)
                    .username(username)
                    .gender(gender)
                    .age(age)
                    .weight(weight)
                    .password(password)
                    .email(email)
                    .build();

            System.out.println("Converted DTO to Entity: " + user);
            return user;
        }
    }

    /**
     * 인증된 사용자 정보를 세션에 저장하기 위한 클래스
     * 직렬화를 사용하여 엔티티와 분리된 세션 데이터를 처리
     */
    @Getter
    @Setter
    public static class Response implements Serializable {

        private final Long id;
        private final String username;
        private final String email;
        private final String gender;
        private final Integer age;
        private final Double weight;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        /* Entity -> DTO */
        public Response(User user) {
            this.id = user.getUserid();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.gender = user.getGender();
            this.age = user.getAge();
            this.weight = user.getWeight();
            this.createdAt = user.getCreatedAt();
            this.updatedAt = user.getUpdatedAt();
        }
    }
    //dto로 변경
    public static UserResponseDTO.Request toUserDTO(User userEntity) {
        return UserResponseDTO.Request.builder()
                .id(userEntity.getUserid())
                .username(userEntity.getUsername())
                .gender(userEntity.getGender())
                .age(userEntity.getAge())
                .weight(userEntity.getWeight())
                .email(userEntity.getEmail())  // 비밀번호 제외
                .build();
    }


}
