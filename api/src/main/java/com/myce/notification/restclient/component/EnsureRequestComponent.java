package com.myce.notification.restclient.component;

import com.myce.global.dto.type.LoginType;
import com.myce.notification.common.ExpoAdminPermission;
import com.myce.notification.dto.email.MailSendContextResponse;
import com.myce.notification.restclient.component.endpoints.RestClientEndPoints;
import com.myce.notification.restclient.service.RestClientService;
import lombok.RequiredArgsConstructor;
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

        restClientService.postAction( RestClientEndPoints.ENSURE_VIEWABLE, body);
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
        restClientService.postAction( RestClientEndPoints.ENSURE_EDITABLE, body);
    }

    public MailSendContextResponse mailSendContextRequest(Long expoId, String entranceStatus, String name,
                                       String phone, String reservationCode, String ticketName) {

        Map<String, Object> body = Map.of(
                "expoId", expoId,
                "entranceStatus", entranceStatus,
                "name", name,
                "phone", phone,
                "reservationCode", reservationCode,
                "ticketName", ticketName
        );

        return restClientService.postAction(
                RestClientEndPoints.MAIL_CONTEXT, body,
                MailSendContextResponse.class).getBody();
    }
}
