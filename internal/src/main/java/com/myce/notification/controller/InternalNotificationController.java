package com.myce.notification.controller;


import com.myce.notification.dto.*;
import com.myce.notification.dto.request.notification.EventReminderRequest;
import com.myce.notification.dto.request.notification.ExpoStartRequest;
import com.myce.notification.dto.request.notification.PaymentCompleteRequest;
import com.myce.notification.dto.request.notification.QrIssuedRequest;
import com.myce.notification.service.NotificationInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
@Slf4j
public class InternalNotificationController {

    private final NotificationInternalService noticeInternalService;

    @PostMapping("/qr-issued")
    public ResponseEntity<Void> sendQrIssued(@RequestBody QrIssuedRequest req) {
        log.info("QR-Issued 요청 수신됨: memberId={}, reservationId={}, expoTitle={}, reissue={}",
                req.getMemberId(), req.getReservationId(), req.getExpoTitle(), req.isReissue());
        noticeInternalService.sendQrIssuedNotification(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payment-completed")
    public ResponseEntity<Void> sendPaymentComplete(@RequestBody PaymentCompleteRequest req) {
        noticeInternalService.sendPaymentCompleteNotification(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/event-reminder")
    public ResponseEntity<Void> sendEventHourReminder(@RequestBody EventReminderRequest req) {
        noticeInternalService.sendEventHourReminderNotification(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/expo-started")
    public ResponseEntity<Void> sendExpoStart(@RequestBody ExpoStartRequest req) {
        noticeInternalService.sendExpoStartNotification(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/expo-status-changed")
    public ResponseEntity<Void> sendExpoStatusChange(@RequestBody ExpoStatusChangeCommand command) {
        noticeInternalService.sendExpoStatusChangeNotification(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ad-status-changed")
    public ResponseEntity<Void> sendAdStatusChange(@RequestBody AdStatusChangeCommand command) {
        noticeInternalService.sendAdvertisementStatusChangeNotification(command);
        return ResponseEntity.noContent().build();
    }

}

