package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "user_cookingstyle")
public class UserCookingStyle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cookingstyle_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "cooking_style", length = 10, nullable = false)
    private String cookingsStyle;

}
