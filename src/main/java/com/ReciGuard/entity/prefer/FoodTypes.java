package com.ReciGuard.entity.prefer;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "food_type")
public class FoodTypes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_type_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_id")
    private Long preferenceId;

    @Column(name = "food_type")
    @Enumerated(EnumType.STRING)
    private String Foodtype;
}
