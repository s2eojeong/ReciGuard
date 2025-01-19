package com.ReciGuard.repository;

import com.ReciGuard.entity.UserCookingStyle;
import com.ReciGuard.entity.UserFoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserFoodTypeRepository extends JpaRepository<UserFoodType,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserFoodType uft WHERE uft.user.id = :userId")
    void deleteByUserId(@Param("userId")Long userid);

    @Query("SELECT uft FROM UserFoodType uft WHERE uft.user.id = :userId")
    List<UserFoodType> findByUserId(@Param("userId") Long userId);
}
