package com.ReciGuard.service;

import com.ReciGuard.entity.Recipe;
import com.ReciGuard.entity.User;
import com.ReciGuard.entity.UserScrap;
import com.ReciGuard.repository.UserScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserScrapService {

    private final UserScrapRepository userScrapRepository;
    private final RecipeStatsService recipeStatsService;

    public boolean toggleScrap(Long userId, Long recipeId) {
        boolean isScrapped = userScrapRepository.existsUserScrap(userId, recipeId);

        if (isScrapped) {
            // 이미 스크랩된 상태 -> 스크랩 해제
            userScrapRepository.deleteUserScrap(userId, recipeId);
            recipeStatsService.updateScrapCount(recipeId, -1); // ScrapCount 감소
            return false; // 스크랩 해제
        } else {
            // 스크랩되지 않은 상태 -> 스크랩 추가
            UserScrap userScrap = new UserScrap();

            userScrap.setUser(new User(userId));
            userScrap.setRecipe(new Recipe(recipeId));
            userScrapRepository.save(userScrap);

            recipeStatsService.updateScrapCount(recipeId, 1); // ScrapCount 증가
            return true; // 스크랩 추가
        }
    }
}
