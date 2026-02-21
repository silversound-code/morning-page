package com.morning.morningpage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정 클래스
 * 
 * 목적:
 * 1. 비밀번호 암호화를 위한 BCryptPasswordEncoder 제공
 * 2. Spring Security 필터 체인 설정
 * 
 * 참고:
 * - 현재는 인증/인가 기능은 세션으로 직접 구현
 * - Spring Security는 PasswordEncoder만 사용
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 비밀번호 암호화 인코더 빈 등록
     * 
     * BCrypt 특징:
     * - 단방향 해시 함수 (복호화 불가능)
     * - Salt 자동 생성 (같은 비밀번호도 매번 다르게 암호화)
     * - Cost 파라미터로 강도 조절 가능 (기본 10)
     * 
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // 강도 지정 시: new BCryptPasswordEncoder(12);
    }
    
    /**
     * Spring Security 필터 체인 설정
     * 
     * 현재 설정:
     * - CSRF 비활성화: REST API이므로 불필요
     * - 모든 요청 허용: 인증은 세션으로 직접 관리
     * 
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception 설정 오류 시
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