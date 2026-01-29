package com.myce.notification.client.component;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.client.config.FeignConfig;
import com.myce.notification.client.feign.ExternalFeignClient;
import com.myce.notification.common.ExpoAdminPermission;
import com.myce.notification.dto.email.EnsureExpoPermissionRequest;
import com.myce.notification.dto.email.MailSendContextRequest;
import com.myce.notification.dto.email.MailSendContextResponse;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.client.component.endpoints.RestClientEndPoints;
import com.myce.notification.client.service.RestClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnsureRequestComponent {

    private final RestClientService restClientService;
    private final ExternalFeignClient externalFeignClient;

    public void viewableEnsureRequest(Long expoId,
                                         Long memberId,
                                         LoginType loginType,
                                         ExpoAdminPermission permission) {

        EnsureExpoPermissionRequest body = new EnsureExpoPermissionRequest(
                expoId, memberId, loginType, permission
        );

        externalFeignClient.ensureViewable(body);

        log.info("[FeignCall] expoId={}, memberId={}, loginType={}, permission={}",
                expoId, memberId, loginType, permission
        );

    }

    public void editableEnsureRequest(Long expoId,
                                         Long memberId,
                                         LoginType loginType,
                                         ExpoAdminPermission permission) {

        Map<String, Object> body = Map.of(
                "expoId", expoId,
                "memberId", memberId,
                "loginType", loginType,
                "permission", permission
        );

        ResponseEntity<Void> response = restClientService.postAction(
                RestClientEndPoints.ENSURE_EDITABLE, body);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(CustomErrorCode.EXPO_EDIT_DENIED);
        }
    }

    public MailSendContextResponse mailSendContextRequest(Long expoId, String entranceStatus, String name,
                                       String phone, String reservationCode, String ticketName) {
        //NULLABLE은 DTO로 보내야 NPE 안걸림
        MailSendContextRequest body = new MailSendContextRequest(
                expoId, entranceStatus, name, phone, reservationCode, ticketName
        );

        ResponseEntity<MailSendContextResponse> response = restClientService.postAction(
                RestClientEndPoints.MAIL_CONTEXT, body,
                MailSendContextResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(CustomErrorCode.MAIL_CONTEXT_FAILED);
        }

        return response.getBody();
    }
}
