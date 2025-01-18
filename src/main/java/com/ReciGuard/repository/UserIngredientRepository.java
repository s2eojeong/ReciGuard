package com.ReciGuard.repository;

import com.ReciGuard.entity.Ingredient;
import com.ReciGuard.entity.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {

    //해당 user의 ingredient 정보를 모두 반환하는 코드
    @Query("SELECT ui FROM UserIngredient ui WHERE ui.user.userid = :userId")
    List<UserIngredient> findByUserId(@Param("userId")Long userId);


    //해당 유저의 ingredient 정보가 있는지 확인하는 코드
    @Query("SELECT ui FROM UserIngredient ui WHERE ui.user.id = :userId AND ui.ingredient.id = :ingredientId")
    Optional<UserIngredient> findByUserIdAndIngredientId(@Param("userId") Long userId, @Param("ingredientId") Long ingredientId);


    @Modifying
    @Transactional
    @Query("DELETE FROM UserIngredient ui WHERE ui.user.id = :userId")
    void deleteByUserId(@Param("userId")Long userid);

    @Query("""
        SELECT i.ingredient
        FROM UserIngredient ui
        JOIN ui.ingredient i
        WHERE ui.user.id = :userId
    """)
    List<String> findAllergyIngredientsByUserId(@Param("userId") Long userId);
}
