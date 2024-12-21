package com.ReciGuard.entity.prefer;

import com.ReciGuard.entity.Enum.CookingStyle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "cooking_style")
public class CookingStyles {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cooking_style_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_id")
    private Long preferenceId;

    @Column(name = "cooking_style")
    @Enumerated(EnumType.STRING)
    private CookingStyle style;
}
