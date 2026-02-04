package com.myce.notification.client.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myce.notification.exception.ErrorResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class FeignExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {

        log.error(
                "[FeignException] status={}, method={}, url={}, body={}",
                e.status(),
                e.request().httpMethod(),
                e.request().url(),
                e.contentUTF8()
        );

        try {
            ErrorResponse error =
                    objectMapper.readValue(e.contentUTF8(), ErrorResponse.class);

            return ResponseEntity
                    .status(e.status())
                    .body(error);

        } catch (Exception ex) {
            log.warn("Feign error body parse failed", ex);

            return ResponseEntity
                    .status(e.status())
                    .body(new ErrorResponse("CORE_ERROR", "Core server error"));
        }
    }
}
