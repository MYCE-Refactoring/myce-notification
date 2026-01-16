package com.myce.notification.restclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${external.base-url.core}")
    private String coreBaseUrl;

    @Value("${gateway.auth.value}")
    private String internalAuthValue;

    @Bean(name = "coreClient")
    public RestClient coreClient() {
        return RestClient.builder()
                .baseUrl(coreBaseUrl)
                .defaultHeader("X-Internal-Auth", internalAuthValue)
                .build();
    }
}

