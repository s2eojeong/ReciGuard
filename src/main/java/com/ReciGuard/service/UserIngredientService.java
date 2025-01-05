package com.ReciGuard.service;

import com.ReciGuard.entity.User;
import com.ReciGuard.entity.UserIngredient;
import com.ReciGuard.repository.UserIngredientRepository;
import com.ReciGuard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserIngredientService {

    private final UserIngredientRepository userIngredientRepository;
    private final UserRepository userRepository;


    public List<UserIngredient> getUserIngredientsByUserId(Long userId) {
        return userIngredientRepository.findByUserId(userId);

    }

    @Transactional
    public void addOrUpdateUserIngredient(Long userId, String ingredient) {
        // Step 1: User 확인
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // Step 2: UserIngredient 확인
        Optional<UserIngredient> existingUserIngredient = userIngredientRepository.findByUserIdAndIngredient(userId, ingredient);

        if (existingUserIngredient.isPresent()) {
            // 이미 존재하면 아무 작업도 하지 않음 (필요시 수정 가능)
            return;
        }

        // Step 3: 존재하지 않으면 새로운 UserIngredient 추가
        UserIngredient newUserIngredient = new UserIngredient();
        newUserIngredient.setUser(user);
        newUserIngredient.setIngredient(ingredient);

        userIngredientRepository.save(newUserIngredient);
    }
}

