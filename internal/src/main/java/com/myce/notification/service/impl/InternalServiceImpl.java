package com.myce.notification.service.impl;

import com.myce.notification.document.Notification;
import com.myce.notification.document.type.AdvertisementStatus;
import com.myce.notification.document.type.ExpoStatus;
import com.myce.notification.document.type.NotificationTargetType;
import com.myce.notification.document.type.NotificationType;
import com.myce.notification.dto.*;
import com.myce.notification.dto.request.*;
import com.myce.notification.entity.MessageTemplateSetting;
import com.myce.notification.entity.type.ChannelType;
import com.myce.notification.entity.type.MessageTemplateCode;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.repository.MessageTemplateSettingRepository;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.InternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.myce.notification.entity.type.MessageTemplateCode.QR_ISSUED;
import static com.myce.notification.entity.type.MessageTemplateCode.QR_REISSUED;
import static com.myce.notification.entity.type.MessageTemplateCode.PAYMENT_COMPLETE;
import static com.myce.notification.entity.type.MessageTemplateCode.EVENT_REMINDER;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalServiceImpl implements InternalService {


    private final NotificationRepository notificationRepository;
    private final RestClient restClient;
    private final MessageTemplateSettingRepository messageTemplateSettingRepository;

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

//        // SSE 실시간 알림 전송
//        String message = String.format(
//                "{\"type\":\"%s\",\"message\":\"%s\"}",
//                type.name(),
//                content
//        );

        SaveNotificationRequest req = SaveNotificationRequest.builder()
                .memberId(memberId)
                .type(type.name())
                .content(content)
                .build();

        restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/sse/notify")
                        .build())
                .body(req)
                .retrieve()
                .toBodilessEntity();


        log.info("알림 저장 및 SSE 전송 완료 - 회원 ID: {}, 제목: {}, 타입: {}", memberId, title, type);
    }

    @Override
    public void sendQrIssuedNotification(QrIssuedRequest req) {

        Long memberId = req.getMemberId();
        Long reservationId = req.getReservationId();
        String expoTitle = req.getExpoTitle();
        boolean isReissue = req.isReissue();


        MessageTemplateCode templateCode = isReissue ? QR_REISSUED : QR_ISSUED;
        MessageTemplateSetting template = messageTemplateSettingRepository
                .findByCodeAndChannelType(templateCode, ChannelType.NOTIFICATION)
                .orElseThrow(() -> new CustomException( CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        String title = template.getSubject();
        String content = template.getContent().replace("{expoTitle}", expoTitle);

        // 공통 saveNotification 메서드 호출
        saveNotification(memberId, reservationId, title, content,
                NotificationType.QR_ISSUED, NotificationTargetType.RESERVATION);

        log.info("QR 발급 알림 처리 완료 - 회원 ID: {}, 예매 ID: {}", memberId, reservationId);

    }

    @Override
    public void sendPaymentCompleteNotification(PaymentCompleteRequest req) {

        String expoTitle = req.getExpoTitle();
        String paymentAmount = req.getPaymentAmount();
        Long memberId = req.getMemberId();
        Long reservationId = req.getReservationId();

        MessageTemplateSetting template = messageTemplateSettingRepository
                .findByCodeAndChannelType(PAYMENT_COMPLETE, ChannelType.NOTIFICATION)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        String title = template.getSubject();
        String content = template.getContent()
                .replace("{expoTitle}", expoTitle)
                .replace("{paymentAmount}", paymentAmount);

        // 공통 saveNotification 메서드 호출
        saveNotification(memberId, reservationId, title, content,
                NotificationType.RESERVATION_CONFIRM, NotificationTargetType.RESERVATION);


        log.info("결제 완료 알림 처리 완료 - 회원 ID: {}, 예매 ID: {}, 금액: {}", memberId, reservationId, paymentAmount);
    }

    @Override
    public void sendExpoStartNotification(ExpoStartRequest req) {

//             메시지 템플릿 조회
        MessageTemplateSetting template = messageTemplateSettingRepository.findByCodeAndChannelType(
                        MessageTemplateCode.EXPO_REMINDER, ChannelType.NOTIFICATION)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        List<Long> userIds = req.getUserIds();
        Long expoId = req.getExpoId();
        String expoTitle = req.getExpoTitle();


        if (userIds.isEmpty()) {
            log.info("알림 전송 대상이 없습니다 - 박람회 ID: {}", expoId);
            return;
        }

        String content = template.getContent().replace("{expoTitle}", expoTitle);

        // 각 예약자에게 알림 전송
        for (Long userId : userIds) {
            saveNotification(
                    userId,
                    expoId,
                    template.getSubject(),
                    content,
                    NotificationType.EXPO_REMINDER,
                    NotificationTargetType.EXPO
            );
        }
        int notificationCount = userIds.size();

        log.info("박람회 시작 알림 처리 완료 - 박람회 ID: {}, 알림 수: {} 개",
                expoId, notificationCount);

    }

    @Override
    public void sendEventHourReminderNotification(EventReminderRequest req) {
            // 메시지 템플릿 조회
            MessageTemplateSetting template = messageTemplateSettingRepository.findByCodeAndChannelType(
                            EVENT_REMINDER, ChannelType.NOTIFICATION)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

            List<Long> memberIds = req.getMemberIds();
            Long expoId = req.getExpoId();
            String expoTitle = req.getExpoTitle();
            String eventName = req.getEventName();
            String startTime = req.getStartTime();


            if (memberIds.isEmpty()) {
                log.info("1시간 전 알림 전송 대상이 없습니다 - 박람회 ID: {}", expoId);
                return;
            }

            String content = template.getContent()
                    .replace("{expoTitle}", expoTitle)
                    .replace("{eventName}", eventName)
                    .replace("{startTime}", startTime);

            // 각 예약자에게 알림 전송
            for (Long userId : memberIds) {
                saveNotification(
                        userId,
                        expoId,
                        template.getSubject(),
                        content,
                        NotificationType.EVENT_REMINDER,
                        NotificationTargetType.EXPO
                );
            }
            int notificationCount = memberIds.size();

            log.info("행사 1시간 전 알림 처리 완료 - 박람회 ID: {}, 알림 수: {} 개",
                    expoId, notificationCount);
    }

    @Override
    public void sendExpoStatusChangeNotification(ExpoStatusChangeCommand command) {
        try {

            MessageTemplateSetting template = messageTemplateSettingRepository.findByCodeAndChannelType(
                            MessageTemplateCode.EXPO_STATUS_CHANGE, ChannelType.NOTIFICATION)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

            Long memberId = command.getMemberId();
            Long expoId = command.getExpoId();
            String expoTitle = command.getExpoTitle();
            ExpoStatus oldStatus = command.getOldStatus();
            ExpoStatus newStatus = command.getNewStatus();


            String title = template.getSubject();
            String content = template.getContent()
                    .replace("{expoTitle}", expoTitle)
                    .replace("{oldStatus}", getStatusDisplayName(oldStatus))
                    .replace("{newStatus}", getStatusDisplayName(newStatus));

            saveNotification(memberId, expoId, title, content,
                    NotificationType.EXPO_STATUS_CHANGE, NotificationTargetType.EXPO_STATUS);


            log.info("박람회 상태 변경 알림 처리 완료 - 박람회 ID: {}, 회원 ID: {}, {} -> {}",
                    expoId, memberId, oldStatus, newStatus);

        } catch (Exception e) {
            log.error("박람회 상태 변경 알림 전송 실패 - 오류: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendAdvertisementStatusChangeNotification(AdStatusChangeCommand command) {
        try {

            MessageTemplateSetting template =
                    messageTemplateSettingRepository.findByCodeAndChannelType(
                            MessageTemplateCode.AD_STATUS_CHANGE,
                            ChannelType.NOTIFICATION
                    ).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

            String content = template.getContent()
                    .replace("{adTitle}", command.getAdTitle())
                    .replace("{oldStatus}", getAdStatusDisplayName(command.getOldStatus()))
                    .replace("{newStatus}", getAdStatusDisplayName(command.getNewStatus()));

            saveNotification(
                    command.getMemberId(),
                    command.getAdId(),
                    template.getSubject(),
                    content,
                    NotificationType.AD_STATUS_CHANGE,
                    NotificationTargetType.AD_STATUS
            );

        } catch (Exception e) {
            log.error("광고 상태 변경 알림 전송 실패", e);
        }
    }

    private String getStatusDisplayName(ExpoStatus status) {
        switch (status) {
            case PENDING_APPROVAL:
                return "승인 대기";
            case PENDING_PAYMENT:
                return "결제 대기";
            case PENDING_PUBLISH:
                return "게시 대기";
            case PENDING_CANCEL:
                return "취소 대기";
            case PUBLISHED:
                return "게시 중";
            case PUBLISH_ENDED:
                return "게시 종료";
            case SETTLEMENT_REQUESTED:
                return "정산 요청";
            case COMPLETED:
                return "종료됨";
            case REJECTED:
                return "승인 거절";
            case CANCELLED:
                return "취소 완료";
            default:
                return status.name();
        }
    }

    private String getAdStatusDisplayName(AdvertisementStatus status) {
        switch (status) {
            case PENDING_APPROVAL:
                return "승인 대기";
            case PENDING_PAYMENT:
                return "결제 대기";
            case PENDING_PUBLISH:
                return "게시 대기";
            case PENDING_CANCEL:
                return "취소 대기";
            case PUBLISHED:
                return "게시 중";
            case COMPLETED:
                return "종료됨";
            case REJECTED:
                return "승인 거절";
            case CANCELLED:
                return "취소 완료";
            default:
                return status.name();
        }
    }

}
