package com.myce.notification.controller;

import com.myce.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;

    @GetMapping(value = "/api/sse/subscribe", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> subscribe() {

        Long memberId = 1L;
        log.trace("initialize Sse Connection for MemberId:" + memberId);
        return ResponseEntity.ok(sseService.subscribe(memberId));
    }

    @PostMapping("/api/sse/notify")
    public ResponseEntity<Void> notify(@RequestParam Long memberId,
                                       @RequestParam String content) {
        sseService.notifyMemberViaSseEmitters(memberId, content);
        log.info("SSE notify 요청: memberId={}, content={}", memberId, content);
        return ResponseEntity.ok().build();
    }

}
