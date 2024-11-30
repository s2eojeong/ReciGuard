package com.ReciGuard.entity.Enum;

public enum Cuisine {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    ITALIAN("양식"),
    ASIAN("아시안");

    private final String name;

    Cuisine(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
