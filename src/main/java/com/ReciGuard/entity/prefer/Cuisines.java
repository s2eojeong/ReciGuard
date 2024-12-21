package com.ReciGuard.entity.prefer;

import com.ReciGuard.entity.Enum.Cuisine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "cuisine")
public class Cuisines {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cusine_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_id")
    private Long preferenceId;

    @Column(name = "cusine")
    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;




}
