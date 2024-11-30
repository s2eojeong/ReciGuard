package com.ReciGuard.entity.Enum;

public enum CookingStyle {
    GRILLED("구이"),
    STEAMED("찜"),
    ROAST("볶음"),
    FRIED("튀김"),
    SOUP("국"),
    STEW("찌개");

    private final String name;

    CookingStyle(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
