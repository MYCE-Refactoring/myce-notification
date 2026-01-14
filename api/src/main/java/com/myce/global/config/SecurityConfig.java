package com.myce.global.config;

import com.myce.global.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("local-gateway")
    private String GATEWAY_AUTH_VALUE;

    @Value("internal-notifications")
    private String INTERNAL_AUTH_VALUE;

    /**
     * HTTP 보안을 구성하고 이를 바탕으로 SecurityFilterChain 빈을 생성한다.
     *
     * 구성 내용: CSRF·폼 로그인·HTTP Basic 비활성화, 세션을 무상태로 설정, 커스텀 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가,
     * 지정된 엔드포인트들에 대해 허용 규칙을 적용하고 그 외 요청은 인증을 요구함.
     *
     * @param http the HttpSecurity 인스턴스 (보안 설정 대상)
     * @return 구성된 SecurityFilterChain
     * @throws Exception 필터 체인 구성 또는 빌드 중 예외가 발생하면 던져짐
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(GATEWAY_AUTH_VALUE, INTERNAL_AUTH_VALUE);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, SecurityEndpoints.GET_PERMIT_ALL).permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityEndpoints.POST_PERMIT_ALL).permitAll()
                        .requestMatchers(HttpMethod.PATCH, SecurityEndpoints.PATCH_PERMIT_ALL).permitAll()
                        .requestMatchers(HttpMethod.DELETE, SecurityEndpoints.DELETE_PERMIT_ALL).permitAll()
                        .requestMatchers(SecurityEndpoints.ETC_PERMIT_ALL).permitAll()
                        .anyRequest().authenticated()
                );



        return http.build();
    }
}