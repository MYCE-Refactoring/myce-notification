package com.myce.notification.restclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RestClientService {

    private final RestClient coreClient;

    public <T> ResponseEntity<Void> postAction(String path, T body) {
        return coreClient.post()
                .uri(path)
                .body(body)
                .retrieve()
                .toEntity(Void.class);
    }

    public <T, R> ResponseEntity<R> postAction(String path, T body, Class<R> responseType) {
        return coreClient.post()
                .uri(path)
                .body(body)
                .retrieve()
                .toEntity(responseType);
    }

    public <B, R> ResponseEntity<R> getAction(String path, B body, Class<R> responseType) {
        return coreClient.post()
                .uri(path)
                .body(body)
                .retrieve()
                .toEntity(responseType);
    }

}
