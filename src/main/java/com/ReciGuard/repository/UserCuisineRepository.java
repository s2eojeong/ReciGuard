package com.ReciGuard.repository;

import com.ReciGuard.entity.UserCookingStyle;
import com.ReciGuard.entity.UserCuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserCuisineRepository extends JpaRepository<UserCuisine,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserCuisine uc WHERE uc.user.id = :userId")
    void deleteByUserId(@Param("userId")Long userid);

    @Query("SELECT uc FROM UserCuisine uc WHERE uc.user.id = :userId")
    List<UserCuisine> findByUserId(@Param("userId") Long userId);
}
