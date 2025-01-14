package com.ReciGuard.service;

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
            // Step 1: Ingredient 찾거나 생성
            Ingredient ingredient = findOrCreateIngredient(ingredientName);

            // Step 2: UserIngredient가 있는지 확인
            Optional<UserIngredient> optionalUserIngredient = userIngredientRepository
                    .findByUserIdAndIngredientId(userId, ingredient.getId());

            UserIngredient userIngredient;
            if (optionalUserIngredient.isPresent()) {
                // 기존 UserIngredient가 존재할 경우
                userIngredient = optionalUserIngredient.get();
            } else {
                // 새 UserIngredient를 생성
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
                userIngredient = new UserIngredient(null, user, ingredient);
            }

            // UserIngredient 저장 또는 갱신
            userIngredientRepository.save(userIngredient);
        }
    }

    public Ingredient findOrCreateIngredient(String ingredientName) {
        return ingredientRepository.findFirstByIngredient(ingredientName)
                .orElseGet(() -> createNewIngredient(ingredientName));
    }

    private Ingredient createNewIngredient(String ingredientName) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredient(ingredientName);
        return ingredientRepository.save(ingredient);
    }


}

