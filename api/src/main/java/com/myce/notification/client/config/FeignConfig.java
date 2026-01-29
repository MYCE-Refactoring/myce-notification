package com.myce.notification.client.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${external.auth.value}")
    private String externalAuthValue;

    @Bean
    public RequestInterceptor externalAuthInterceptor() {
        return template ->
                template.header("X-Internal-Auth", externalAuthValue);
    }
}

