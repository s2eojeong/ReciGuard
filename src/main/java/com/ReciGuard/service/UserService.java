package com.ReciGuard.service;

import com.ReciGuard.dto.UserResponseDTO;
import com.ReciGuard.entity.User;
import com.ReciGuard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


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
        User user = userRequestDTO.toEntity();

        userRepository.save(user);
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
}

