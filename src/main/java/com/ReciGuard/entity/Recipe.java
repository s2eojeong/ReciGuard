package com.ReciGuard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
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
    @BatchSize(size = 10)
    private List<Instruction> instructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @BatchSize(size = 10)
    private List<RecipeIngredient> recipeIngredients;

    @OneToOne(mappedBy = "recipe",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Nutrition nutrition;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RecipeStats recipeStats;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<UserScrap> userScraps;

    public Recipe(Long id) {
        this.id = id;
    }
}
