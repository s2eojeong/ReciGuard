package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "user_cuisine")

public class UserCuisine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cuisine_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "food_type", length = 10, nullable = false)
    private String foodType;

}
