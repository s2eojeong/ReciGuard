package com.ReciGuard.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 빌더 패턴 추가
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userid;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String gender;

    private Integer age;

    @Column(precision = 4, scale = 1)
    private Double weight;

    @Column(name = "user_pw", length = 20, nullable = false)
    private String password;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void modify(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long userid) {
        this.userid = userid;
    }
}

