package com.myce.notification.controller;


import com.myce.notification.dto.*;
import com.myce.notification.dto.request.EventReminderRequest;
import com.myce.notification.dto.request.ExpoStartRequest;
import com.myce.notification.dto.request.PaymentCompleteRequest;
import com.myce.notification.dto.request.QrIssuedRequest;
import com.myce.notification.service.NotificationInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
@Slf4j
public class InternalNotificationController {

    private final NotificationInternalService noticeInternalService;

    @PostMapping("/qr-issued")
    public void sendQrIssued(@RequestBody QrIssuedRequest req) {
        log.info("QR-Issued 요청 수신됨: memberId={}, reservationId={}, expoTitle={}, reissue={}",
                req.getMemberId(), req.getReservationId(), req.getExpoTitle(), req.isReissue());
        noticeInternalService.sendQrIssuedNotification(req);
    }

    @PostMapping("/payment-completed")
    public void sendPaymentComplete(@RequestBody PaymentCompleteRequest req) {
        noticeInternalService.sendPaymentCompleteNotification(req);
    }

    @PostMapping("/event-reminder")
    public void sendEventHourReminder(@RequestBody EventReminderRequest req) {
        noticeInternalService.sendEventHourReminderNotification(req);
    }

    @PostMapping("/expo-started")
    public void sendExpoStart(@RequestBody ExpoStartRequest req) {
        noticeInternalService.sendExpoStartNotification(req);
    }

    @PostMapping("/expo-status-changed")
    public void sendExpoStatusChange(@RequestBody ExpoStatusChangeCommand command) {
        noticeInternalService.sendExpoStatusChangeNotification(command);
    }

    @PostMapping("/ad-status-changed")
    public void sendAdStatusChange(@RequestBody AdStatusChangeCommand command) {
        noticeInternalService.sendAdvertisementStatusChangeNotification(command);
    }

}

