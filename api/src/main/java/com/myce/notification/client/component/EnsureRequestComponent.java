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

import static com.myce.notification.exception.CustomErrorCode.*;

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
        try{externalFeignClient.ensureViewable(body);}
        catch(Exception e) {
            throw new CustomException(EXPO_VIEW_DENIED);
        }


            log.info("[FeignCall] expoId={}, memberId={}, loginType={}, permission={}",
                expoId, memberId, loginType, permission
        );

    }

    public void editableEnsureRequest(Long expoId,
                                         Long memberId,
                                         LoginType loginType,
                                         ExpoAdminPermission permission) {

        EnsureExpoPermissionRequest body = new EnsureExpoPermissionRequest(
                expoId, memberId, loginType, permission
        );

        try{
            externalFeignClient.ensureEditable(body);
        }
        catch(Exception e) {
            throw new CustomException(EXPO_EDIT_DENIED);
        }
    }

    public MailSendContextResponse mailSendContextRequest(Long expoId, String entranceStatus, String name,
                                       String phone, String reservationCode, String ticketName) {

        MailSendContextRequest body = new MailSendContextRequest(
                expoId, entranceStatus, name, phone, reservationCode, ticketName
        );

        try{
            return externalFeignClient.mailContext(body);
        } catch(Exception e) {
            throw new CustomException(MAIL_CONTEXT_FAILED);
        }
    }
}
