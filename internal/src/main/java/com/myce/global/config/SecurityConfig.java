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

    @Value("${gateway.auth.value}")
    private String GATEWAY_AUTH_VALUE;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(GATEWAY_AUTH_VALUE);

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