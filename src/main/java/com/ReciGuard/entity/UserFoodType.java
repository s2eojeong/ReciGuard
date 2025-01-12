package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_foodtype")
@NoArgsConstructor
@Getter @Setter
@Builder
@AllArgsConstructor
public class UserFoodType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_foodtype_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "food_type", length = 10, nullable = false)
    private String foodType;

}
