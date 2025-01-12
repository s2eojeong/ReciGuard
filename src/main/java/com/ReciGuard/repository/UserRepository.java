package com.ReciGuard.repository;

import com.ReciGuard.entity.User;
import com.ReciGuard.entity.UserScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /* Security - 로그인 시 사용자 검색 */
    Optional<User> findByUsername(String username);
    /* email로 사용자 조회 */
    Optional<User> findByEmail(String email);

    /* 중복 검사: 중복인 경우 true, 중복되지 않은 경우 false 리턴 */
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    /* 회원정보 수정 */
    //@Override
    //<S extends User> S save(S entity);

    /* 로그인 검증 */
    Optional<User> findByUsernameAndPassword(String username, String password);

    // 사용자 ID로 스크랩 데이터 조회
    @Query("SELECT s FROM UserScrap s WHERE s.user.id = :userId")
    List<UserScrap> findScrapsByUserId(@Param("userId") Long userId);
}
