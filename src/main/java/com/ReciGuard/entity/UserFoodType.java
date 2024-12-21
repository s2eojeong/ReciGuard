package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_foodtype")
@NoArgsConstructor
@Getter @Setter
public class UserFoodType {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Long userId;

    @Column(name = "food_type", length = 10, nullable = false)
    private String foodType;

}
