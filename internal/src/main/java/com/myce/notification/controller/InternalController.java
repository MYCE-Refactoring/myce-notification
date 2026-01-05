package com.myce.notification.controller;


import com.myce.notification.document.Notification;
import com.myce.notification.dto.QrIssuedRequest;
import com.myce.notification.repository.NotificationRepository;
import com.myce.notification.service.InternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
@Slf4j
public class InternalController {

    private final InternalService notificationService; // 기존 구현 주입
    private final NotificationRepository notificationRepository;

    @PostMapping("/qr-issued")
    public void sendQrIssued(@RequestBody QrIssuedRequest req) {
        log.info("QR-Issued 요청 수신됨: memberId={}, reservationId={}, expoTitle={}, reissue={}",
                req.getMemberId(), req.getReservationId(), req.getExpoTitle(), req.isReissue());
        notificationService.sendQrIssuedNotification(req);
    }

    @GetMapping("")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifi = notificationRepository.findAll();

        log.info("알림 전체 조회 API 호출됨, 개수: {}", notifi.size());

        return ResponseEntity.ok(notifi);
    }

}

