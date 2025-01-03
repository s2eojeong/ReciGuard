package com.ReciGuard.controller;
import com.ReciGuard.SecurityConfig.UserPrincipal;
import com.ReciGuard.dto.UserResponseDTO;
import com.ReciGuard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // 본인 확인 (id와 인증된 사용자 비교)
        if (!userPrincipal.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 서비스 호출
        userService.deleteUser(id);

        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }

}



