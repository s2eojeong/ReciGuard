package com.ReciGuard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
public class RecipeStats {
    @Id @GeneratedValue
    @Column(name = "stats_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Date date;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "scrap_count")
    private int scrapCount;
}
