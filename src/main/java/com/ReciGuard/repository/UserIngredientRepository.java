package com.ReciGuard.repository;

import com.ReciGuard.entity.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {

    //해당 user의 ingredient 정보를 모두 반환하는 코드
    @Query("SELECT ui FROM UserIngredient ui WHERE ui.user.userid = :userId")
    List<UserIngredient> findByUserId(Long userId);


    //해당 유저의 ingredient 정보가 있는지 확인하는 코드
    @Query("SELECT ui FROM UserIngredient ui WHERE ui.user.id = :userId AND ui.ingredient = :ingredient")
    Optional<UserIngredient> findByUserIdAndIngredient(@Param("userId") Long userId, @Param("ingredient") String ingredient);
}
