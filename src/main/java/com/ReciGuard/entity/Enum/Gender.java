package com.ReciGuard.entity.Enum;

public enum Gender {
    MAN("남자"),
    WOMAN("여자");

    private final String description;

    // 생성자
    Gender(String description) {
        this.description = description;
    }

    // Getter 메서드
    public String getDescription() {
        return description;
    }
}
