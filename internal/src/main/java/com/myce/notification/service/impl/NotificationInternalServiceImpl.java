package com.myce.notification.service.impl;

import com.myce.notification.document.Notification;
import com.myce.notification.document.type.ExpoStatus;
import com.myce.notification.document.type.NotificationTargetType;
import com.myce.notification.document.type.NotificationType;
import com.myce.notification.dto.*;
import com.myce.notification.dto.request.*;
import com.myce.notification.dto.request.notification.EventReminderRequest;
import com.myce.notification.dto.request.notification.ExpoStartRequest;
import com.myce.notification.dto.request.notification.PaymentCompleteRequest;
import com.myce.notification.dto.request.notification.QrIssuedRequest;
import com.myce.notification.entity.type.MessageTemplateCode;
import com.myce.notification.repository.MessageTemplateSettingRepository;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.MessageTemplateService;
import com.myce.notification.service.NotificationInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

import static com.myce.notification.entity.type.MessageTemplateCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationInternalServiceImpl implements NotificationInternalService {


    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, SendNotificationRequest> kafkaTemplate;
    private final MessageTemplateService messageTemplateService;

    @Value("${app.kafka.topics.notification-sse}")
    private String notificationSseTopic;

    @Override
    public void sendQrIssuedNotification(QrIssuedRequest req) {

        Long memberId = req.getMemberId();
        Long reservationId = req.getReservationId();
        String expoTitle = req.getExpoTitle();
        boolean isReissue = req.isReissue();

        MessageTemplateCode templateCode = isReissue ? QR_REISSUED : QR_ISSUED;

        MessageFormat format = messageTemplateService.renderMessage(templateCode,
                Map.of("expoTitle", expoTitle));

        String title = format.subject();
        String content = format.content();

        saveNotification(memberId, reservationId, title, content,
                NotificationType.QR_ISSUED, NotificationTargetType.RESERVATION);

        sendNotification(memberId, NotificationType.QR_ISSUED, content);

    }

    @Override
    public void sendPaymentCompleteNotification(PaymentCompleteRequest req) {

        String expoTitle = req.getExpoTitle();
        String paymentAmount = req.getPaymentAmount();
        Long memberId = req.getMemberId();
        Long reservationId = req.getReservationId();

        MessageFormat format = messageTemplateService.renderMessage(PAYMENT_COMPLETE,
                Map.of("expoTitle", expoTitle,
                        "paymentAmount", paymentAmount));

        String title = format.subject();
        String content = format.content();

        saveNotification(memberId, reservationId, title, content,
                NotificationType.RESERVATION_CONFIRM, NotificationTargetType.RESERVATION);

        sendNotification(memberId, NotificationType.RESERVATION_CONFIRM, content);
    }

    @Override
    public void sendExpoStartNotification(ExpoStartRequest req) {

        List<Long> memberIds = req.getUserIds();
        Long expoId = req.getExpoId();
        String expoTitle = req.getExpoTitle();

        if (memberIds.isEmpty()) {
            return;
        }
        MessageFormat format = messageTemplateService.renderMessage(EXPO_REMINDER,
                Map.of("expoTitle", expoTitle));

        String content = format.content();
        String title = format.subject();

        for (Long memberId : memberIds) {
            saveNotification(
                    memberId,
                    expoId,
                    title,
                    content,
                    NotificationType.EXPO_REMINDER,
                    NotificationTargetType.EXPO
            );
            sendNotification(memberId, NotificationType.EXPO_REMINDER, content);
        }
    }

    @Override
    public void sendEventHourReminderNotification(EventReminderRequest req) {

        List<Long> memberIds = req.getMemberIds();
        Long expoId = req.getExpoId();
        String expoTitle = req.getExpoTitle();
        String eventName = req.getEventName();
        String startTime = req.getStartTime();

        if (memberIds.isEmpty()) {
            log.info("1시간 전 알림 전송 대상이 없습니다 - 박람회 ID: {}", expoId);
            return;
        }

        MessageFormat format = messageTemplateService.renderMessage(EVENT_REMINDER,
                Map.of("expoTitle", expoTitle,
                        "eventName", eventName,
                        "startTime", startTime));

        String content = format.content();
        String subject = format.subject();

        for (Long memberId : memberIds) {
            saveNotification(
                    memberId,
                    expoId,
                    subject,
                    content,
                    NotificationType.EVENT_REMINDER,
                    NotificationTargetType.EXPO);

            sendNotification(memberId, NotificationType.EVENT_REMINDER, content);
        }
    }

    @Override
    public void sendExpoStatusChangeNotification(ExpoStatusChangeCommand command) {

        Long memberId = command.getMemberId();
        Long expoId = command.getExpoId();
        String expoTitle = command.getExpoTitle();
        ExpoStatus oldStatus = command.getOldStatus();
        ExpoStatus newStatus = command.getNewStatus();

        MessageFormat format = messageTemplateService.renderMessage(EXPO_STATUS_CHANGE,
                Map.of("expoTitle", expoTitle,
                        "oldStatus", oldStatus.getLabel(),
                        "newStatus", newStatus.getLabel()));

        String content = format.content();
        String title = format.subject();

        saveNotification(memberId, expoId, title, content,
                NotificationType.EXPO_STATUS_CHANGE, NotificationTargetType.EXPO_STATUS);

        sendNotification(memberId, NotificationType.EXPO_STATUS_CHANGE, content);
    }

    @Override
    public void sendAdvertisementStatusChangeNotification(AdStatusChangeCommand command) {

        MessageFormat format = messageTemplateService.renderMessage(AD_STATUS_CHANGE,
                Map.of("adTitle", command.getAdTitle(),
                        "oldStatus", command.getOldStatus().getLabel(),
                        "newStatus", command.getNewStatus().getLabel()));

        saveNotification(
                command.getMemberId(),
                command.getAdId(),
                format.subject(),
                format.content(),
                NotificationType.AD_STATUS_CHANGE,
                NotificationTargetType.AD_STATUS
        );

        sendNotification(command.getMemberId(), NotificationType.AD_STATUS_CHANGE, format.content());
    }


    private void saveNotification(Long memberId, Long targetId, String title, String content,
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
    }

    private void sendNotification(Long memberId, NotificationType type, String content) {

        SendNotificationRequest req = SendNotificationRequest.builder()
                .memberId(memberId)
                .type(type.name())
                .content(content)
                .build();

        kafkaTemplate.send(notificationSseTopic, String.valueOf(memberId), req);
    }
}
