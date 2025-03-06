package com.ReciGuard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipe", indexes = @Index(name = "idx_recipe_cuisine", columnList = "cuisine"))
public class Recipe {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
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

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Instruction> instructions = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToOne(mappedBy = "recipe",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Nutrition nutrition;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RecipeStats recipeStats;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserScrap> userScraps = new ArrayList<>();

    public Recipe(Long recipeId) {
        this.id=getId();
    }
}
