package com.JtoP.Spring.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login") // 사용자가 정의한 로그인 페이지를 사용
                        .defaultSuccessUrl("/")); // 성공시 루트로 이동

                return http.build();// 모든 경로에 대해 인증 없이 접근 허용
    }

    @Bean
        // 비밀번호 암호와 검증
        // 인증 시 암호화된 비밀번호를 비교
        // Bcrypt 알고리즘 사용
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // 기본 설정으로 모든 요청에 대해 인증을 요구함.
}