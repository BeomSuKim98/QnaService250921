package com.JtoP.Spring.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(new AntPathRequestMatcher("/question/list")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/question/detail/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user/signup")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/style.css")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
//            .usernameParameter("logindId")  아이디 필드 변경
//            .passwordParameter("password")  비밀번호 필드 변경
                        .loginPage("/user/login") // 사용자가 정의한 로그인 페이지를 사용

                        // POST
                        .loginProcessingUrl("/user/login") // 로그인 처리 경로
                        .defaultSuccessUrl("/")) // 성공시 루트로 이동

                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    @Bean
        // 비밀번호 암호와 검증
        // 인증 시 암호화된 비밀번호를 비교
        // Bcrypt 알고리즘 사용
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // 기본 설정으로 모든 요청에 대해 인증을 요구함.

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

        // AuthenticationManager는 인증을 처리하는 핵심 인터페이스
        // AuthenticationManager는 사용자의 자격 증명을 확인하고 인증된 사용자에 대한 정보를 반환
        // AuthenticationManager는 다양한 인증 방법(폼 로그인, OAuth2 등)을 지원
        // 그냥 외우기
    }
}