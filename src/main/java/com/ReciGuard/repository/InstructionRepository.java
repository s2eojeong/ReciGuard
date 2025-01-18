package com.ReciGuard.repository;

import com.ReciGuard.entity.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    @Query("SELECT MAX(i.instructionId) FROM Instruction i WHERE i.recipe.id = :recipeId")
    Optional<Integer> findMaxInstructionIdByRecipeId(@Param("recipeId") Long recipeId);

}
