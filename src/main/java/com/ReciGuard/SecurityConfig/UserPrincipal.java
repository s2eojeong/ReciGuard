package com.ReciGuard.SecurityConfig;

import com.ReciGuard.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    // 엔티티(User)를 기반으로 UserPrincipal 생성
    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList() // 권한을 따로 설정하지 않을 경우
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // 권한 리스트 반환
    }

    @Override
    public String getPassword() {
        return password; // 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return username; // 사용자 이름 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}

