package com.ReciGuard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
public class RecipeStats {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "scrap_count")
    private int scrapCount;
}
