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
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JsonIgnore
    private List<RecipeIngredient> recipeIngredient;

    private String ingredient;
}
