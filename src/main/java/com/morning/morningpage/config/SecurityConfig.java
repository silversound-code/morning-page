package com.morning.morningpage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정
 * 목적: 비밀번호 암호화를 위한 BCryptPasswordEncoder 제공
 * 참고: 현재는 인증/인가 기능은 세션으로 직접 구현
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 비밀번호 암호화 인코더
     * BCrypt: 단방향 해시 함수, Salt 자동 생성
     * 같은 비밀번호도 매번 다르게 암호화됨
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Spring Security 필터 체인 설정
     * 모든 요청 허용 (우리가 세션으로 직접 관리)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // 모든 요청 허용
            );
        return http.build();
    }
}