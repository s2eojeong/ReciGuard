package com.ReciGuard.controller;
import com.ReciGuard.SecurityConfig.UserPrincipal;
import com.ReciGuard.dto.UserIngredientDTO;
import com.ReciGuard.dto.UserResponseDTO;
import com.ReciGuard.entity.UserIngredient;
import com.ReciGuard.service.UserIngredientService;
import com.ReciGuard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    //회원정보 조회
    @GetMapping("/{userid}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userid) {
        try {
            // 서비스 호출
            UserResponseDTO.Response userInfo = userService.getUserInfo(userid);
            return ResponseEntity.ok(userInfo); // 성공 시 사용자 정보 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 사용자 정보 없을 시
        }
    }

    //회원 탈퇴
    @DeleteMapping("/{userid}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userid, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // 본인 확인 (id와 인증된 사용자 비교)
        if (!userPrincipal.getId().equals(userid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 서비스 호출
        userService.deleteUser(userid);

        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }

    //allergy 조회
    @GetMapping("/allergy/{userId}")
    public List<UserIngredientDTO> getUserIngredients(@PathVariable Long userId) {
        List<UserIngredient> userIngredients = userIngredientService.getUserIngredientsByUserId(userId);
        return userIngredients.stream()
                .map(UserIngredientDTO::fromEntity)//dto로 변경
                .collect(Collectors.toList());
    }

    @PostMapping("/allergy/{userId}")
    public ResponseEntity<String> addOrUpdateUserIngredient(@PathVariable Long userId, @RequestParam String ingredient) {
        userIngredientService.addOrUpdateUserIngredient(userId, ingredient);
        return ResponseEntity.ok("UserIngredient 추가 또는 갱신 완료");
    }
    /*추가적으로 정해야할 것 어떤 정보를 줘서 ingredient를 수정하게 할 것인지
    예를 들어 "계란"이라는 정보만 줘서 초기화해서 추가할 건지 아니면 체크방식? 등으로 바꿀 건지

     */

}



