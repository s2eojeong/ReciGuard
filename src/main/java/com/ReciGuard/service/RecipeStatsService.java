package com.ReciGuard.service;

import com.ReciGuard.entity.RecipeStats;
import com.ReciGuard.repository.RecipeStatsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeStatsService {

    private final RecipeStatsRepository recipeStatsRepository;

    // 뷰 카운트 증가
    @Transactional
    public void increaseViewCount(Long recipeId) {
        RecipeStats stats = recipeStatsRepository.findByRecipe_Id(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("RecipeStats not found for recipeId: " + recipeId));
        stats.increaseViewCount();
        // recipeStatsRepository.save(stats); -> 필요 x
        // 영속성 컨텍스트 내의 엔티티는 트랜잭션 커밋 시 자동으로 flush 되기 때문
    }

    // 스크랩 카운트 증가 또는 감소
    @Transactional
    public void updateScrapCount(Long recipeId, int increment) {
        RecipeStats stats = recipeStatsRepository.findByRecipe_Id(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("RecipeStats not found for recipeId: " + recipeId));
        stats.updateScrapCount(increment);
    }
}
