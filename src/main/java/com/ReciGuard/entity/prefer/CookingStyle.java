package com.ReciGuard.entity.prefer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter

public class CookingStyle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cooking_style_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_id")
    private Long preferenceId;

    @Column(name = "cooking_style", length = 10, nullable = false)
    private String style;
}
