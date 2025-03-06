package com.example.shop.config;

import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 이 클래스는 Spring Security 설정을 위한 구성 클래스임을 나타냄
//@EnableMethodSecurity(
//        prePostEnabled = true,  // @PreAuthorize, @PostAuthorize 애너테이션을 활성화합니다.
//        securedEnabled = true,  // @Secured 애너테이션을 활성화합니다.
//        jsr250Enabled = true    // @RolesAllowed 애너테이션을 활성화합니다.
//)
@EnableMethodSecurity
@EnableWebSecurity
@WebListener
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(
                // URL을 통해서 들어오는 인가, 권한 에 따른 접속 여부
                        (AuthorizeHttpRequests) -> AuthorizeHttpRequests
                                // 해당 url을 모든 사용자가 접속가능
                                .requestMatchers("/user/login/**", "/user/signUp", "/css/**", "/js/**", "/").permitAll()
                                // 권한이 ADMIN인 사람은 해당페이지에 접속가능
                                .requestMatchers("/admin/item/**").hasRole("ADMIN")
                                .requestMatchers("/orders/**" , "/cart/**").authenticated() // 로그인이 되어있어만 하는 주소

                                .anyRequest().permitAll()

                )
                // csrf 토큰 : 서버에서 생성되는 임의 값으로 페이지 요청시 항상
                // 다른 값으로 생성된다.
                // 토크 : 요청을 식별하고 검증하는데 사용하는 특수한 문자열 또는 값
                // 미지정시 csrf 방어기능에 의한 접근 거부
                .csrf(csrf -> csrf.disable())

                .formLogin(
                        formLogin -> formLogin.loginPage("/user/login") // 로그인 페이지 지정
                                .defaultSuccessUrl("/")                 // 로그인성공시 이동페이지
                                .usernameParameter("email")//.failureUrl("/user/login")
                                // 로그인시 작성 Parameter명               // 실패시 url
                )
                .logout(logout -> logout.logoutUrl("/user/logout")
                        .logoutSuccessUrl("/").invalidateHttpSession(true) // 로그아웃시 이동할 url, 로그아웃 url , 로그아웃시 세션초기화
                )
                .exceptionHandling(
                        a -> a.accessDeniedHandler(new CustomAccessDeniedHandler())
                );

                return http.build();

        // 로그인 : 인증

        // 로그아웃




        // 로그인과 로그아웃시 페이지 접속시 각각 handler 지정
        // 기본적으로 제공하는 기능과 다르게 직접 설정

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




}
