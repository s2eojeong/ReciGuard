package com.ReciGuard.service;

import com.ReciGuard.dto.ScrapRecipeDTO;
import com.ReciGuard.entity.Recipe;
import com.ReciGuard.entity.User;
import com.ReciGuard.entity.UserScrap;
import com.ReciGuard.repository.RecipeRepository;
import com.ReciGuard.repository.UserScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            return false;
        } else {
            // 스크랩되지 않은 상태 -> 스크랩 추가
            UserScrap userScrap = UserScrap.builder()
                    .user(User.builder().userId(userId).build())
                    .recipe(Recipe.builder().id(recipeId).build())
                    .build();

            userScrapRepository.save(userScrap);
            recipeStatsService.updateScrapCount(recipeId, 1); // ScrapCount 증가
            return true;
        }
    }
    public List<ScrapRecipeDTO> getScrappedRecipesByUser(Long userId) {
        // UserScrap 리스트 조회
        List<UserScrap> userScraps = userScrapRepository.findAllByUser_UserId(userId);

        return userScraps.stream()
                .map(scrap -> {
                    boolean scrapped = userScrapRepository.existsUserScrap(userId, scrap.getRecipe().getId());
                    return new ScrapRecipeDTO(
                            scrap.getRecipe().getId(),
                            scrap.getRecipe().getRecipeName(),
                            scrap.getCreatedAt(),
                            scrapped,
                            scrap.getRecipe().getImagePath()
                            );
                })
                .collect(Collectors.toList());
    }
}
