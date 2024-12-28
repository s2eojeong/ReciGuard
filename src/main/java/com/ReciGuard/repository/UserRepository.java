package com.ReciGuard.repository;

import com.ReciGuard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /* Security - 로그인 시 사용자 검색 */
    Optional<User> findByUsername(String username);

    /* 중복 검사: 중복인 경우 true, 중복되지 않은 경우 false 리턴 */
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


    /* 회원정보 수정 */
    @Override
    <S extends User> S save(S entity);

    /* 로그인 검증 */
    Optional<User> findByUsernameAndPassword(String username, String password);
}
