package com.ReciGuard.entity.prefer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Cuisine{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cusine_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_id")
    private Long preferenceId;

    @Column(length = 10, nullable = false)
    private String cusine;



}
