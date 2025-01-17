package KGUcapstone.OutDecision.global.security.config;

import KGUcapstone.OutDecision.global.security.filter.JwtAuthFilter;
import KGUcapstone.OutDecision.global.security.filter.JwtExceptionFilter;
import KGUcapstone.OutDecision.global.security.handler.CustomLoginSuccessHandler;
import KGUcapstone.OutDecision.global.security.handler.CustomAuthenticationFailureHandler;
import KGUcapstone.OutDecision.global.security.handler.CustomAuthenticationSuccessHandler;
import KGUcapstone.OutDecision.domain.user.service.auth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomAuthenticationFailureHandler oAuth2LoginFailureHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // HTTP 기본 인증을 비활성화
                .cors().and() // CORS 활성화
                .csrf().disable() // CSRF 보호 기능 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션관리 정책을 STATELESS(세션이 있으면 쓰지도 않고, 없으면 만들지도 않는다)

        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(customLoginSuccessHandler)
                );

        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();

        http
                .authorizeHttpRequests()
                .anyRequest().permitAll();

        http
                .exceptionHandling()
                .accessDeniedPage("/error");

        http
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService) // OAuth2 로그인시 사용자 정보를 가져오는 엔드포인트와 사용자 서비스를 설정
                        .and()
                        .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패시 처리할 핸들러를 지정해준다.
                        .successHandler(oAuth2LoginSuccessHandler) // OAuth2 로그인 성공시 처리할 핸들러를 지정해준다.
                        .permitAll()
                );


        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다.
        return http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }
}