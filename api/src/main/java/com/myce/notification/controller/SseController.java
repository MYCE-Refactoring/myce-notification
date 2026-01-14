package com.myce.notification.controller;

import com.myce.global.dto.CustomUserDetails;
import com.myce.notification.dto.request.SendNotificationRequest;
import com.myce.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;

    @GetMapping(value = "/api/notifications/sse/subscribe", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        return ResponseEntity.ok(sseService.subscribe(memberId));
    }

    @PostMapping("/api/notifications/sse/notify")
    public ResponseEntity<Void> notify(@RequestBody SendNotificationRequest req) {
        sseService.notifyMemberViaSseEmitters(req.getMemberId(), req.getContent());
        return ResponseEntity.ok().build();
    }

}
