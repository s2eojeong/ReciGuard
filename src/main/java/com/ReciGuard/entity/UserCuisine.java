package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "user_cuisine")

public class UserCuisine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cuisine_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "cuisine", length = 10, nullable = false)
    private String cuisine;

}
