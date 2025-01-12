package com.ReciGuard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Recipe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "recipe_name")
    private String recipeName;

    private int serving;

    private String cuisine;
    private String foodType;
    private String cookingStyle;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instruction> instructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredients;

    @OneToOne(mappedBy = "recipe", fetch = FetchType.LAZY)
    private Nutrition nutrition;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RecipeStats recipeStats;

    public Recipe(Long id) {
        this.id = id;
    }

    // 연관관계 메서드
    public void setRecipeStats(RecipeStats stats) {
        this.recipeStats = stats;
        stats.setRecipe(this); // 양방향 연관관계 설정
    }
}
