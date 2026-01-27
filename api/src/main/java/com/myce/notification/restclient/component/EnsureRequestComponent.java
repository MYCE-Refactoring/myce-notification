package com.myce.notification.restclient.component;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.common.ExpoAdminPermission;
import com.myce.notification.dto.email.MailSendContextRequest;
import com.myce.notification.dto.email.MailSendContextResponse;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.restclient.component.endpoints.RestClientEndPoints;
import com.myce.notification.restclient.service.RestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EnsureRequestComponent {

    private final RestClientService restClientService;

    public void viewableEnsureRequest(Long expoId,
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
                RestClientEndPoints.ENSURE_VIEWABLE, body);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(CustomErrorCode.EXPO_VIEW_DENIED);
        }

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
