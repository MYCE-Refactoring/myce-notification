package com.myce.notification.consumer;

import com.myce.notification.dto.request.SendNotificationRequest;
import com.myce.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final SseService sseService;

//    @KafkaListener(topics = "${app.kafka.topics.notification-sse}", groupId = "notification-sse-consumer")
//    public void consume(SendNotificationRequest req) {
//        sseService.notifyMemberViaSseEmitters(req.getMemberId(), req.getContent());
//        log.info("SSE Event Occur: memberId={}, type={}", req.getMemberId(), req.getType());
//    }
}