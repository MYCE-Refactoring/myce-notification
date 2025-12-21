package com.myce.notification.controller;

import com.myce.notification.dto.NotificationResponse;
import com.myce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        Long memberId = 1L;
        List<NotificationResponse> notifications = notificationService.getNotificationsByMemberId(memberId);
        return ResponseEntity.ok(notifications);
    }

}