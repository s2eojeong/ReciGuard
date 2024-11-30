package com.ReciGuard.entity.Enum;

public enum FoodType {
    RICE("밥"),
    NOODLE("면"),
    BREAD("빵"),
    RICECAKE("떡"),
    PORRIDGE("죽");

    private final String name;

    FoodType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
