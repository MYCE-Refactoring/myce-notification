package com.myce.notification.controller;

import com.myce.notification.dto.NotificationResponseList;
import com.myce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<NotificationResponseList> getNotifications(
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        Long memberId = 1L;
        NotificationResponseList list =
                notificationService.getNotificationsByMemberId(memberId, page);
        return ResponseEntity.ok(list);
    }


    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable String notificationId) {
        Long memberId = 1L;
        notificationService.markAsRead(notificationId, memberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        Long memberId = 1L;
        notificationService.markAllAsRead(memberId);
        return ResponseEntity.ok().build();
    }
}