package com.ReciGuard.service;

import com.ReciGuard.dto.UserResponseDTO;
import com.ReciGuard.entity.User;
import com.ReciGuard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UserResponseDTO.Request userRequestDTO) {

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
                .build();

        userRepository.save(user);
    }

}
