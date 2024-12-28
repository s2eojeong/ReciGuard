package com.ReciGuard.repository;

import com.ReciGuard.entity.UserScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScrapRepository extends JpaRepository<UserScrap, Long> {

    // 사용자가 특정 레시피를 스크랩했는지 확인
    @Query("""
        SELECT COUNT(us) > 0
        FROM UserScrap us
        WHERE us.user.id = :userId AND us.recipe.id = :recipeId
    """)
    boolean existsUserScrap(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    // 사용자 스크랩 정보 삭제
    @Modifying
    @Query("""
        DELETE FROM UserScrap us
        WHERE us.user.id = :userId AND us.recipe.id = :recipeId
    """)
    void deleteUserScrap(@Param("userId") Long userId, @Param("recipeId") Long recipeId);
}
