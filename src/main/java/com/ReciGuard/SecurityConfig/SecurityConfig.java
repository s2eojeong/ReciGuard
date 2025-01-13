package com.ReciGuard.SecurityConfig;

import com.ReciGuard.JWT.JWTFilter;
import com.ReciGuard.JWT.JWTUtil;
import com.ReciGuard.JWT.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) -> auth.disable()); // CSRF 비활성화
        http
                .cors(); // CORS 설정 활성화 (WebConfig의 설정 반영)
        http
                .formLogin((auth) -> auth.disable()); // 기본 로그인 폼 비활성화

        http
                .httpBasic((auth) -> auth.disable()); // HTTP Basic 인증 비활성화

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/password", "/register").permitAll() // 인증 불필요한 경로
                        .anyRequest().authenticated()); // 나머지 요청은 인증 필요

        // JWTFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // 커스텀 로그인 필터 등록
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션을 Stateless로 설정

        return http.build();
    }
}
