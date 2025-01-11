package com.ReciGuard.repository;

import com.ReciGuard.entity.UserFoodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFoodTypeRepository extends JpaRepository<UserFoodType,Long> {
}
