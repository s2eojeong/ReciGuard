package com.ReciGuard.repository;

import com.ReciGuard.entity.Recipe;
import com.ReciGuard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // 오늘의 추천 레시피
    @Query("""
        SELECT DISTINCT r 
        FROM Recipe r
        LEFT JOIN FETCH r.recipeStatsList rs
        WHERE r.id = :recipeId
    """)
    Recipe findTodayRecipe(@Param("recipeId") Long recipeId);

    // 전체 레시피 리스트 -> 필터링 후
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        LEFT JOIN r.recipeIngredients ri
        LEFT JOIN ri.ingredient i
        LEFT JOIN UserIngredient ui ON i.id = ui.ingredient.id
        WHERE ui.user.id IS NULL OR ui.user.id != :userId
    """)
    List<Recipe> findAllFilteredRecipes(@Param("userId") Long userId);

    // cuisine 별로 레시피 리스트 검색
    List<Recipe> findByCuisine(String cuisine);

    // cuisine 별로 레시피 리스트 검색 -> 필터링 후
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        LEFT JOIN r.recipeIngredients ri
        LEFT JOIN ri.ingredient i
        LEFT JOIN UserIngredient ui ON i.id = ui.ingredient.id
        WHERE (ui.user.id IS NULL OR ui.user.id != :userId)
          AND r.cuisine = :cuisine
    """)
    List<Recipe> findCuisineFilteredRecipes(@Param("userId") Long userId, @Param("cuisine") String cuisine);

    // 검색 단어 필터링해서 레시피 리스트 검색
    @Query("""
        SELECT DISTINCT r 
        FROM Recipe r
        JOIN r.ingredients i
        WHERE r.recipeName LIKE %:query% OR i.name LIKE %:query%
    """)
    List<Recipe> findByQuery(@Param("query") String query); // 리스트로 받아오기

    // 검색 단어와 사용자 알레르기 정보를 기반으로 필터링된 레시피 리스트 검색
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        LEFT JOIN r.recipeIngredients ri
        LEFT JOIN ri.ingredient i
        LEFT JOIN UserIngredient ui ON i.id = ui.ingredient.id
        WHERE (ui.user.id IS NULL OR ui.user.id != :userId)
          AND (r.recipeName LIKE %:query% OR i.name LIKE %:query%)
    """)
    List<Recipe> findQueryFilteredRecipes(@Param("userId") Long userId, @Param("query") String query);

    // 특정 레시피 상세 정보
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        LEFT JOIN FETCH r.instructions i
        LEFT JOIN FETCH r.recipeIngredients ri
        LEFT JOIN FETCH ri.ingredient ing
        LEFT JOIN FETCH r.nutrition n
        WHERE r.id = :id
    """)
    Optional<Recipe> findRecipeDetailById(@Param("id") Long id); // 리스트로 받아오기

    // 특정 사용자가 등록한 레시피 조회
    List<Recipe> findAllByUser(User user);
}
