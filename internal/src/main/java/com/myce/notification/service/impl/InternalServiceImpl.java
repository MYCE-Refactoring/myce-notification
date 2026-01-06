package com.myce.notification.service.impl;

import com.myce.notification.config.RestClientConfig;
import com.myce.notification.document.Notification;
import com.myce.notification.document.type.NotificationTargetType;
import com.myce.notification.document.type.NotificationType;
import com.myce.notification.dto.QrIssuedRequest;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.InternalService;
import com.myce.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalServiceImpl implements InternalService {


    private final NotificationRepository notificationRepository;
    private final RestClient restClient;

    @Override
    public void saveNotification(Long memberId, Long targetId, String title, String content,
                                 NotificationType type, NotificationTargetType targetType) {

            Notification notification = Notification.builder()
                    .memberId(memberId)
                    .type(type)
                    .targetType(targetType)
                    .targetId(targetId)
                    .title(title)
                    .content(content)
                    .isRead(false)
                    .build();

            notificationRepository.save(notification);

            // SSE 실시간 알림 전송
            String message = String.format(
                    "{\"type\":\"%s\",\"message\":\"%s\"}",
                    type.name(),
                    content
            );

            restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/sse/notify")
                            .queryParam("memberId", memberId)
                            .queryParam("content", message)
                            .build())
                    .retrieve()
                    .toBodilessEntity();


            log.info("알림 저장 및 SSE 전송 완료 - 회원 ID: {}, 제목: {}, 타입: {}", memberId, title, type);
    }

    @Override
    public void sendQrIssuedNotification(QrIssuedRequest req) {

            Long memberId = req.getMemberId();
            Long reservationId = req.getReservationId();


//            MessageTemplateCode templateCode = isReissue ? QR_REISSUED : QR_ISSUED;
//            MessageTemplateSetting template = messageTemplateSettingRepository
//                    .findByCodeAndChannelType(templateCode, ChannelType.NOTIFICATION)
//                    .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

            String title = "title";
            String content = req.getExpoTitle();

            // 공통 saveNotification 메서드 호출
            saveNotification(memberId, reservationId, title, content,
                    NotificationType.QR_ISSUED, NotificationTargetType.RESERVATION);

            log.info("QR 발급 알림 처리 완료 - 회원 ID: {}, 예매 ID: {}", memberId, reservationId);

    }

}
