package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Ingredient {
    @Id @GeneratedValue
    @Column(name = "ingredient_id")
    private Long id;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private RecipeIngredient recipeIngredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergy_id")
    private Allergy allergy;

    private String ingredient;
}
