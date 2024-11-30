package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Allergy {
    @Id @GeneratedValue
    @Column(name = "allergy_id")
    private Long id;

    @OneToOne(mappedBy = "allergy", fetch = FetchType.LAZY)
    private Ingredient ingredient;

    @Column(name = "allergy_name")
    private String allergyName;
}
