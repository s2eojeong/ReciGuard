package com.ReciGuard.controller;
import com.ReciGuard.SecurityConfig.UserPrincipal;
import com.ReciGuard.dto.*;
import com.ReciGuard.entity.Ingredient;
import com.ReciGuard.entity.User;
import com.ReciGuard.entity.UserIngredient;
import com.ReciGuard.service.UserIngredientService;
import com.ReciGuard.service.UserScrapService;
import com.ReciGuard.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserIngredientService userIngredientService;
    private final UserScrapService userScrapService;

    //회원정보 조회
    @GetMapping("/{userid}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userid) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            // 서비스 호출
            Long findUserId = userService.findUserIdByUsername(username);
            if (!findUserId.equals(userid))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다");

            UserResponseDTO.Response userInfo = userService.getUserInfo(userid);
            return ResponseEntity.ok(userInfo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //회원 탈퇴
    @DeleteMapping("/{userid}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userid) {
        // 본인 확인 (id와 인증된 사용자 비교)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Long findUserId = userService.findUserIdByUsername(username);
            if (!findUserId.equals(userid))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다");
            userService.deleteUser(userid);
            return ResponseEntity.ok("회원탈퇴가 완료되었습니다."); // 성공 시 사용자 정보 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 사용자 정보 없을 시
        }

    }

    //allergy 조회
    @GetMapping("/allergy/{userid}")
    public ResponseEntity<List<UserIngredientDTO>> getUserIngredients(@PathVariable Long userid) {
        List<UserIngredient> userIngredients = userIngredientService.getUserIngredientsByUserId(userid);
        List<UserIngredientDTO> userIngredientDTOs = userIngredients.stream()
                .map(UserIngredientDTO::fromEntity) // DTO로 변환
                .collect(Collectors.toList());

        return ResponseEntity.ok(userIngredientDTOs);
    }


    //알레르기 정보 수정
    @PostMapping("/allergy/{userid}")
    public ResponseEntity<String> addOrUpdateUserIngredients(
            @PathVariable Long userid, @RequestBody UserIngredientListDTO userIngredientListDTO) {
        // Step 1: 서비스 호출
        userIngredientService.addOrUpdateUserIngredients(userid, userIngredientListDTO);

        // Step 2: 응답 반환
        return ResponseEntity.ok("UserIngredient 추가 또는 갱신 완료");
    }

    //스크랩 레시피 찾기
    @GetMapping("/scraps")
    public List<ScrapRecipeDTO> getUserScrappedRecipes(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userService.findUserIdByUsername(username);

        return userScrapService.getScrappedRecipesByUser(userId);
    }

    @PutMapping("/info/{userid}")
    public ResponseEntity<?> updateUserInfo(@Valid @RequestBody UserUpdateDTO.Request userDTO, @PathVariable Long userid, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(bindingResult.getFieldError().getDefaultMessage());
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long findUserId = userService.findUserIdByUsername(username);
        if (!userid.equals(findUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다");

        }
        userDTO.setUsername(username);
        userService.updateUserInfo(userDTO);
        return ResponseEntity.ok(userDTO);
    }
    @GetMapping("/info/{userid}")
    public ResponseEntity<?> updateGetUserInfo(@PathVariable Long userid) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            // 서비스 호출
            Long findUserId = userService.findUserIdByUsername(username);
            if (!findUserId.equals(userid))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다");

            UserUpdateDTO.Response userInfo = userService.updateGetUserInfo(userid);
            return ResponseEntity.ok(userInfo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}





