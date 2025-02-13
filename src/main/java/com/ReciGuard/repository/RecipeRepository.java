package com.ReciGuard.repository;

import com.ReciGuard.dto.MyRecipeForm;
import com.ReciGuard.entity.Instruction;
import com.ReciGuard.entity.Recipe;
import com.ReciGuard.entity.RecipeIngredient;
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
        SELECT r
        FROM Recipe r
        WHERE r.id = :recipeId
    """)
    Recipe findTodayRecipe(@Param("recipeId") Long recipeId);

    // 전체 레시피 리스트 -> 필터링 후
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        WHERE NOT EXISTS (
            SELECT 1
            FROM RecipeIngredient ri
            JOIN Ingredient i ON ri.ingredient.id = i.id
            WHERE ri.recipe.id = r.id
            AND EXISTS (
                SELECT 1
                FROM UserIngredient ui
                JOIN Ingredient ing ON ui.ingredient.id = ing.id
                WHERE ui.user.id = :userId
                AND (i.id = ing.id
                     OR i.ingredient LIKE CONCAT('%', ing.ingredient, '%'))
            )
        )
    """)
    List<Recipe> findAllFilteredRecipes(@Param("userId") Long userId);

    // cuisine 별로 레시피 리스트 검색
    List<Recipe> findByCuisine(String cuisine);

    // cuisine 별로 레시피 리스트 검색 -> 필터링 후
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        WHERE r.cuisine = :cuisine
        AND NOT EXISTS (
            SELECT 1
            FROM RecipeIngredient ri
            JOIN Ingredient i ON ri.ingredient.id = i.id
            WHERE ri.recipe.id = r.id
            AND EXISTS (
                SELECT 1
                FROM UserIngredient ui
                JOIN Ingredient i2 ON ui.ingredient.id = i2.id
                WHERE ui.user.id = :userId
                AND (
                    i2.ingredient = i.ingredient
                    OR i.ingredient LIKE CONCAT('%', i2.ingredient, '%')
                )
            )
        )
    """)
    List<Recipe> findCuisineFilteredRecipes(@Param("userId") Long userId, @Param("cuisine") String cuisine);

    // 검색 단어 필터링해서 레시피 리스트 검색
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        JOIN r.recipeIngredients ri
        JOIN ri.ingredient i
        WHERE r.recipeName LIKE %:query% OR i.ingredient LIKE %:query%
    """)
    List<Recipe> findByQuery(@Param("query") String query);

    // 검색 단어와 사용자 알레르기 정보를 기반으로 필터링된 레시피 리스트 검색
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        WHERE (r.recipeName LIKE CONCAT('%', :query, '%'))
        AND NOT EXISTS (
            SELECT 1
            FROM RecipeIngredient ri
            JOIN Ingredient i ON ri.ingredient.id = i.id
            WHERE ri.recipe.id = r.id
            AND EXISTS (
                SELECT 1
                FROM UserIngredient ui
                JOIN Ingredient i2 ON ui.ingredient.id = i2.id
                WHERE ui.user.id = :userId
                AND (
                    i2.ingredient = i.ingredient
                    OR i.ingredient LIKE CONCAT('%', i2.ingredient, '%')
                )
            )
        )
    """)
    List<Recipe> findQueryFilteredRecipes(@Param("userId") Long userId, @Param("query") String query);

    // 특정 레시피 상세 정보
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        LEFT JOIN FETCH r.nutrition n
        WHERE r.id = :id
    """)
    Optional<Recipe> findRecipeById(@Param("id") Long id);

    // 특정 사용자가 등록한 레시피 조회 (userId 사용)
    @Query("SELECT r FROM Recipe r WHERE r.user.id = :userId")
    List<Recipe> findAllByUserId(@Param("userId") Long userId);

    // 사용자가 작성한 나만의 레시피 조회
    @Query("SELECT r FROM Recipe r WHERE r.id = :recipeId AND r.user.id = :userId")
    Optional<Recipe> findRecipeByUserId(@Param("recipeId") Long recipeId, @Param("userId") Long userId);
}
