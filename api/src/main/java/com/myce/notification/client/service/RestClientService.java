package com.myce.notification.client.service;

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
                .exchange((req, res) -> ResponseEntity
                        .status(res.getStatusCode())
                        .build() );
    }

    public <T, R> ResponseEntity<R> postAction(String path, T body, Class<R> responseType) {
        return coreClient.post()
                .uri(path)
                .body(body)
                .exchange((req, res) -> {
                    R responseBody = null;

                    if (res.getStatusCode().is2xxSuccessful()) {
                        responseBody = res.bodyTo(responseType);
                    }

                    return ResponseEntity
                            .status(res.getStatusCode())
                            .body(responseBody);
                });
    }

    public <B, R> ResponseEntity<R> getAction(String path, B body, Class<R> responseType) {
        return coreClient.post()
                .uri(path)
                .body(body)
                .retrieve()
                .toEntity(responseType);
    }

}
