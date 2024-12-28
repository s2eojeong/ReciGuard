package com.ReciGuard.repository;

import com.ReciGuard.entity.RecipeStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeStatsRepository extends JpaRepository<RecipeStats, Long> {

    // View count 증가
    @Modifying
    @Query("""
        UPDATE RecipeStats rs
        SET rs.viewCount = rs.viewCount + 1
        WHERE rs.recipe.id = :recipeId
    """)
    void updateViewCount(@Param("recipeId") Long recipeId);

    // Scrap count 증가 또는 감소
    @Modifying
    @Query("""
        UPDATE RecipeStats rs
        SET rs.scrapCount = rs.scrapCount + :increment
        WHERE rs.recipe.id = :recipeId AND rs.scrapCount + :increment >= 0
    """)
    void updateScrapCount(@Param("recipeId") Long recipeId, @Param("increment") int increment);
}
