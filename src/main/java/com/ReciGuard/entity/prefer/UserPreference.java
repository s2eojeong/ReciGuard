package com.ReciGuard.entity.prefer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserPreference {

    @Id @GeneratedValue
    @Column(name = "preference_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Long userId;

}
