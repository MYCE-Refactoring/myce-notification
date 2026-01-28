package com.myce.notification.restclient.config;

import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Value("${external.base-url.core}")
    private String coreBaseUrl;

    @Value("${external.auth.value}")
    private String externalAuthValue;

    @Bean(name = "coreClient")
    public RestClient coreClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder
                .baseUrl(coreBaseUrl)
                .defaultHeader("X-Internal-Auth", externalAuthValue)
                .build();
    }
}

