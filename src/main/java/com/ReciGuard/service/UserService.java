package com.ReciGuard.service;

import com.ReciGuard.dto.UserPasswordDTO;
import com.ReciGuard.dto.UserResponseDTO;
import com.ReciGuard.dto.UserUpdateDTO;
import com.ReciGuard.entity.*;
import com.ReciGuard.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCookingStyleRepository userCookingStyleRepository;
    private final UserCuisineRepository userCuisineRepository;
    private final UserFoodTypeRepository userFoodTypeRepository;
    private final UserIngredientRepository userIngredientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IngredientRepository ingredientRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public void save(UserResponseDTO.Request userRequestDTO) {

        String username = userRequestDTO.getUsername();
        String password = userRequestDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .age(userRequestDTO.getAge())
                .email(userRequestDTO.getEmail())
                .weight(userRequestDTO.getWeight())
                .gender(userRequestDTO.getGender())
                .build();

        userRepository.save(user);

        //cookingstyle 저장
        userRequestDTO.getUserCookingStyle().forEach(cookingStyle -> {
            UserCookingStyle userCookingStyle = UserCookingStyle.builder()
                    .cookingsStyle(cookingStyle)
                    .user(user)
                    .build();
            userCookingStyleRepository.save(userCookingStyle);
        });

        //cuisine 저장
        userRequestDTO.getUserCuisine().forEach(cuisine -> {
            UserCuisine userCuisine = UserCuisine.builder()
                    .cuisine(cuisine)
                    .user(user)
                    .build();
            userCuisineRepository.save(userCuisine);
        });

        //foodtype 저장
        userRequestDTO.getUserFoodType().forEach(foodType -> {
            UserFoodType userFoodType = UserFoodType.builder()
                    .foodType(foodType)
                    .user(user)
                    .build();
            userFoodTypeRepository.save(userFoodType);
        });


        List<UserIngredient> userIngredients = userRequestDTO.getIngredients().stream()
                .map(ingredientName -> {
                    // Ingredient를 데이터베이스에서 조회 또는 생성
                    Ingredient ingredient = ingredientRepository.findByIngredient(ingredientName);

                    if (ingredient == null) {
                        ingredient = new Ingredient();
                        ingredient.setIngredient(ingredientName); // 이름 설정
                        ingredient = ingredientRepository.save(ingredient); // 저장 후 반환
                    }

                    // UserIngredient 엔티티 생성 및 설정
                    UserIngredient userIngredient = new UserIngredient();
                    userIngredient.setUser(user);
                    userIngredient.setIngredient(ingredient);
                    return userIngredient;
                })
                .collect(Collectors.toList());

        // UserIngredient 리스트를 저장
        userIngredientRepository.saveAll(userIngredients);
    }

    //회원 정보 수정(비밀번호)
    @Transactional
    public void modify(UserResponseDTO.Request userDTO) {
        User user = userRepository.findById(userDTO.toEntity().getUserid()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        String encPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        user.modify(encPassword);
    }

    //로그인
    @Transactional
    public UserResponseDTO.Request login(UserResponseDTO.Request userDTO) {
        // 회원가입 때 입력한 email로 해당 user의 정보를 가져옴
        Optional<User> byEmailUser = userRepository.findByEmail(userDTO.getEmail());
        // 입력한 email의 user 정보가 있으면
        if (byEmailUser.isPresent()) {
            User userEntity = byEmailUser.get();
            // 사용자가 입력한 password와 회원가입 때 입력한 password를 비교함
            if (bCryptPasswordEncoder.matches(userDTO.getPassword(), userEntity.getPassword())) {
                // 같으면 해당 user 정보를 dto로 변환하여 return
                return UserResponseDTO.toUserDTO(userEntity);
            } else {
                // password가 다르면 null return
                return null;
            }
        } else {
            // 입력한 email로 저장된 user가 없으면
            // 즉 입력한 email로 회원가입을 하지 않았다면 null return
            return null;
        }
    }

    //회원 정보 조회
    public UserResponseDTO.Response getUserInfo(Long userid) {
        User user = userRepository.findById(userid).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        //entity를 dto로 변환해서 전달
        return new UserResponseDTO.Response(user);
    }

    //회원 탈퇴 로직
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        userRepository.delete(user); // 실제 삭제 예시
    }

    // username 기반으로 userId 조회
    public Long findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."))
                .getUserid();
    }

    public void changePassword(UserPasswordDTO passwordDTO) {
        // 사용자 조회
        Optional<User> optionalUser = userRepository.findByUsername(passwordDTO.getUsername());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with username: " + passwordDTO.getUsername());
        }

        User user = optionalUser.get();

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // 새 비밀번호 저장
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserInfo(UserUpdateDTO.Request userDTO) {
        final String username = userDTO.getUsername();
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with username:" + username));

        user.setGender(userDTO.getGender());
        user.setAge(userDTO.getAge());
        user.setWeight(userDTO.getWeight());

        userCookingStyleRepository.deleteByUserId(user.getUserid());
        List<UserCookingStyle> cookingStyles = userDTO.getUserCookingStyle().stream()
                .map(style -> UserCookingStyle.builder()
                        .cookingsStyle(style)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        userCookingStyleRepository.saveAll(cookingStyles);

        // Step 4: Cuisine 업데이트
        userCuisineRepository.deleteByUserId(user.getUserid());
        List<UserCuisine> cuisines = userDTO.getUserCuisine().stream()
                .map(cuisine -> UserCuisine.builder()
                        .cuisine(cuisine)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        userCuisineRepository.saveAll(cuisines);

        // Step 5: Food Type 업데이트
        userFoodTypeRepository.deleteByUserId(user.getUserid());
        List<UserFoodType> foodTypes = userDTO.getUserFoodType().stream()
                .map(foodType -> UserFoodType.builder()
                        .foodType(foodType)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        userFoodTypeRepository.saveAll(foodTypes);

        userIngredientRepository.deleteByUserId(user.getUserid());

        User finduser = userRepository.findOneByUserName(user.getUsername());
        userDTO = UserUpdateDTO.toUserDTO(finduser);

    }
    public UserUpdateDTO.Response updateGetUserInfo(Long userid) {
        User user = userRepository.findById(userid).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        //entity를 dto로 변환해서 전달
        List<UserCookingStyle> cookingStyles = userCookingStyleRepository.findByUserId(userid);
        List<UserFoodType> foodTypes = userFoodTypeRepository.findByUserId(userid);
        List<UserCuisine> cuisines = userCuisineRepository.findByUserId(userid);

        user.setCookingStyles(cookingStyles);
        user.setFoodTypes(foodTypes);
        user.setCuisines(cuisines);
        return new UserUpdateDTO.Response(user);
    }
}


