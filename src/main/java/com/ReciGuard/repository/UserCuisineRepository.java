package com.ReciGuard.repository;

import com.ReciGuard.entity.UserCuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserCuisineRepository extends JpaRepository<UserCuisine,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserCuisine uc WHERE uc.user.id = :userId")
    void deleteByUserId(@Param("userId")Long userid);
}
