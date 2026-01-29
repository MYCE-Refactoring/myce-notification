package com.myce.notification.client.config;

import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();

        if (status == 403) {
            if (methodKey.contains("ensureEditable")) {
                return new CustomException(CustomErrorCode.EXPO_EDIT_DENIED);
            }
            if (methodKey.contains("ensureViewable")) {
                return new CustomException(CustomErrorCode.EXPO_VIEW_DENIED);
            }
        }

        if (methodKey.contains("mailContext")) {
            return new CustomException(CustomErrorCode.MAIL_CONTEXT_FAILED);
        }

        if (status == 400) {
            return new CustomException(CustomErrorCode.CORE_CLIENT_ERROR);
        }

        if (status >= 500) {
            return new CustomException(CustomErrorCode.CORE_SERVER_ERROR);
        }

        return new CustomException(CustomErrorCode.CORE_CLIENT_ERROR);
    }
}

