package com.myce.notification.controller;


import com.myce.notification.dto.*;
import com.myce.notification.dto.request.EventReminderRequest;
import com.myce.notification.dto.request.ExpoStartRequest;
import com.myce.notification.dto.request.PaymentCompleteRequest;
import com.myce.notification.dto.request.QrIssuedRequest;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.InternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
@Slf4j
public class InternalController {

    private final InternalService internalService;

    @PostMapping("/qr-issued")
    public void sendQrIssued(@RequestBody QrIssuedRequest req) {
        log.info("QR-Issued 요청 수신됨: memberId={}, reservationId={}, expoTitle={}, reissue={}",
                req.getMemberId(), req.getReservationId(), req.getExpoTitle(), req.isReissue());
        internalService.sendQrIssuedNotification(req);
    }

    @PostMapping("/payment-completed")
    public void sendPaymentComplete(@RequestBody PaymentCompleteRequest req) {
        internalService.sendPaymentCompleteNotification(req);
    }

    @PostMapping("/event-reminder")
    public void sendEventHourReminder(@RequestBody EventReminderRequest req) {
        internalService.sendEventHourReminderNotification(req);
    }

    @PostMapping("/expo-started")
    public void sendExpoStart(@RequestBody ExpoStartRequest req) {
        internalService.sendExpoStartNotification(req);
    }

    @PostMapping("/expo-status-changed")
    public void sendExpoStatusChange(@RequestBody ExpoStatusChangeCommand command) {
        internalService.sendExpoStatusChangeNotification(command);
    }

    @PostMapping("/ad-status-changed")
    public void sendAdStatusChange(@RequestBody AdStatusChangeCommand command) {
        internalService.sendAdvertisementStatusChangeNotification(command);
    }

}

