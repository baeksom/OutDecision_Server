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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 비활성화
                .cors(cors -> {}) // CORS 활성화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 관리 정책 설정
                );

        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(customLoginSuccessHandler)
                );

        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/public/**", "/login", "/signup").permitAll() // 특정 경로만 허용
                                .anyRequest().authenticated() // 나머지 요청은 인증 필요
                );

        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedPage("/error")
                );

        http
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization") // OAuth2 로그인 페이지 설정
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(customOAuth2UserService)
                        )
                        .failureHandler(oAuth2LoginFailureHandler)
                        .successHandler(oAuth2LoginSuccessHandler)
                        .permitAll()
                );

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class);

        return http.build();
    }
}
