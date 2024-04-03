package KGUcapstone.OutDecision.global.config.security;

import KGUcapstone.OutDecision.domain.user.filter.JwtAuthFilter;
import KGUcapstone.OutDecision.domain.user.filter.JwtExceptionFilter;
import KGUcapstone.OutDecision.domain.user.handler.CustomLoginSuccessHandler;
import KGUcapstone.OutDecision.domain.user.handler.MyAuthenticationFailureHandler;
import KGUcapstone.OutDecision.domain.user.handler.MyAuthenticationSuccessHandler;
import KGUcapstone.OutDecision.domain.user.service.OAuth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // HTTP 기본 인증을 비활성화
                .cors().and() // CORS 활성화
                .csrf().disable() // CSRF 보호 기능 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션관리 정책을 STATELESS(세션이 있으면 쓰지도 않고, 없으면 만들지도 않는다)
                .and()
            .authorizeRequests() // 요청에 대한 인증 설정
                .requestMatchers("/loginSuccess").permitAll()
                .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                .requestMatchers("/login-test").permitAll()
                .requestMatchers("/token/**").permitAll() // 토큰 발급을 위한 경로는 모두 허용
                .requestMatchers("/registerSuccess").permitAll()
                .requestMatchers("/user/register/**").permitAll()
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**").permitAll()
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요하다.
                .and()
            .oauth2Login() // OAuth2 로그인 설정시작
                .userInfoEndpoint().userService(customOAuth2UserService) // OAuth2 로그인시 사용자 정보를 가져오는 엔드포인트와 사용자 서비스를 설정
                .and()
                .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패시 처리할 핸들러를 지정해준다.
                .successHandler(oAuth2LoginSuccessHandler)
                .permitAll() // OAuth2 로그인 성공시 처리할 핸들러를 지정해준다.
                .and()
            .formLogin() // 단, 로그인 기능은 인증없이 허용
                .permitAll()
                .successHandler(customLoginSuccessHandler)
                .and()
            .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/").permitAll();// 단, 로그아웃 기능은 인증없이 허용


//        http.formLogin(Customizer.withDefaults());
//
//        http.csrf(csrf -> csrf.disable());

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다.
        return http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsManager() {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);
        UserDetails user = userBuilder
                .username("user")
                .password("asdf")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }



}