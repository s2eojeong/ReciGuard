package com.ReciGuard.repository;

import com.ReciGuard.entity.UserCookingStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserCookingStyleRepository extends JpaRepository<UserCookingStyle, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserCookingStyle ucs WHERE ucs.user.id = :userId")
    void deleteByUserId(@Param("userId")Long userid);

    @Query("SELECT ucs FROM UserCookingStyle ucs WHERE ucs.user.id = :userId")
    List<UserCookingStyle> findByUserId(@Param("userId") Long userId);

}
