package com.ReciGuard.service;

import com.ReciGuard.dto.UserIngredientDTO;
import com.ReciGuard.dto.UserIngredientListDTO;
import com.ReciGuard.entity.Ingredient;
import com.ReciGuard.entity.User;
import com.ReciGuard.entity.UserIngredient;
import com.ReciGuard.repository.IngredientRepository;
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
    private final IngredientRepository ingredientRepository;


    public List<UserIngredient> getUserIngredientsByUserId(Long userId) {
        return userIngredientRepository.findByUserId(userId);

    }

    @Transactional
    public void addOrUpdateUserIngredients(Long userId, UserIngredientListDTO userIngredientDTO) {
        for (String ingredientName : userIngredientDTO.getIngredients()) {
            // Step 2: Ingredient가 없으면 새로 추가
            Ingredient ingredient = ingredientRepository.findByIngredient(ingredientName);
            if (ingredient == null) {
                // Ingredient가 존재하지 않을 경우 새로 생성하여 저장
                ingredient = new Ingredient();
                ingredient.setIngredient(ingredientName);
                ingredientRepository.save(ingredient);
            }

            // Step 3: UserIngredient가 있는지 확인
            Optional<UserIngredient> optionalUserIngredient = userIngredientRepository
                    .findByUserIdAndIngredientId(userId, ingredient.getId());


            // Step 4: UserIngredient가 없으면 추가, 있으면 업데이트
            UserIngredient userIngredient;
            if (optionalUserIngredient.isPresent()) {
                // 기존 UserIngredient가 존재할 경우
                userIngredient = optionalUserIngredient.get();
                userIngredient.setIngredient(ingredient); // 필요한 경우 ingredient 업데이트
            } else {
                // 새 UserIngredient를 생성
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
                userIngredient = new UserIngredient(null, user, ingredient);
            }

// 저장 또는 갱신
            userIngredientRepository.save(userIngredient);

        }
    }

}

