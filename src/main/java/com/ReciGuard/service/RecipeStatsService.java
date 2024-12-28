package com.ReciGuard.service;

import com.ReciGuard.repository.RecipeStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeStatsService {

    private final RecipeStatsRepository recipeStatsRepository;

    // View count 증가
    public void increaseViewCount(Long recipeId) {
        recipeStatsRepository.updateViewCount(recipeId);
    }

    // Scrap count 증가 또는 감소
    public void updateScrapCount(Long recipeId, int increment) {
        recipeStatsRepository.updateScrapCount(recipeId, increment);
    }
}
