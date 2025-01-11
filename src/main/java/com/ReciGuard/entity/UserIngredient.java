package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "user_ingredient")
@Builder
@AllArgsConstructor
public class UserIngredient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ingredient_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient; // 나중에 ingredient로 바꾸어 주어야함

}
