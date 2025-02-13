package com.ReciGuard.repository;

import com.ReciGuard.entity.RecipeStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecipeStatsRepository extends JpaRepository<RecipeStats, Long> {

    // 레시피 stats 정보 찾기
    Optional<RecipeStats> findByRecipe_Id(Long recipeId);
}
