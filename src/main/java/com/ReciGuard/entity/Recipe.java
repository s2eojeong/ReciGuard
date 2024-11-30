package com.ReciGuard.entity;

import com.ReciGuard.entity.Enum.CookingStyle;
import com.ReciGuard.entity.Enum.Cuisine;
import com.ReciGuard.entity.Enum.FoodType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Recipe {
    @Id @GeneratedValue
    @Column(name = "recipe_id")
    private Long id;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "cooking_time")
    private int cookingTime;

    private int serving;

    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;
    @Enumerated(EnumType.STRING)
    private FoodType foodType;
    @Enumerated(EnumType.STRING)
    private CookingStyle cookingStyle;

    @OneToOne(mappedBy = "recipe", fetch = FetchType.LAZY)
    private Instruction instruction;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeIngredients;

    @OneToOne(mappedBy = "recipe", fetch = FetchType.LAZY)
    private Nutrition nutrition;
}
