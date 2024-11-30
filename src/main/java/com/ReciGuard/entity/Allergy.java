package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Allergy {
    @Id @GeneratedValue
    @Column(name = "allergy_id")
    private Long id;

    @Column(name = "allergy_name")
    private String allergyName;

    @OneToMany(mappedBy = "allergy")
    private List<Ingredient> ingredients;
}
