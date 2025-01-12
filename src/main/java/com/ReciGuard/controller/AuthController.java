package com.ReciGuard.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.ReciGuard.JWT.JWTUtil;
import com.ReciGuard.dto.UserResponseDTO;
import com.ReciGuard.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> save(@Valid @RequestBody UserResponseDTO.Request userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(bindingResult.getFieldError().getDefaultMessage());
        }

        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다.");

    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserResponseDTO.Request userDTO) {
        // 로그인 성공하면 user 정보를, 실패하면 null을 가져옴
        System.out.println("Email: " + userDTO.getEmail());

        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일을 입력해주세요.");
        }
        UserResponseDTO.Request loginResult = userService.login(userDTO);
        if (loginResult != null) {
            String token = jwtUtil.createJwt(userDTO.getEmail(), "ROLE_USER", 60 * 60 * 1000L); // 토큰 생성
            Map<String, String> responseBody = Map.of(
                    "message", "로그인 성공",
                    "token", token
            );
            return ResponseEntity.ok(responseBody);
        } else {
            // 로그인 실패 시 메시지 반환
            return ResponseEntity.status(401).body("로그인에 실패했습니다.");
        }
    }

    // 비밀번호 변경
    @PostMapping("/password")
    public ResponseEntity<?> changePasswordByEmail(@RequestBody UserResponseDTO.Request userDTO) {
        try {
            userService.modify(userDTO);
            return ResponseEntity.ok().body("비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
