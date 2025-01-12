package com.ReciGuard.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 URL 경로에 대해 CORS 허용
                .allowedOriginPatterns("*") // 와일드카드 패턴으로 모든 출처 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 쿠키 인증 허용
    }
}
