package com.myce.notification.service.impl;

import com.myce.notification.service.EmitterRepository;
import com.myce.notification.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseServiceImpl implements SseService {
    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;
    private static final String SSE_CONNECTED = "SSE connected, emitterId: ";
    private static final String KEEP_ALIVE = "keep-alive";

    public SseEmitter subscribe(Long memberId) {
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId, sseEmitter);

        sseEmitter.onCompletion(() -> {
            log.info("sseEmitter completion, emitterId: {}", emitterId);
            emitterRepository.removeSseEmitter(emitterId);
        });

        sseEmitter.onTimeout(() -> {
            log.info("sseEmitter timeout, emitterId: {}", emitterId);
            emitterRepository.removeSseEmitter(emitterId);
        });

        sendMessage(sseEmitter, SSE_CONNECTED + emitterId);

        return sseEmitter;
    }

    public void notifyMemberViaSseEmitters(Long memberId, String content) {
        List<SseEmitter> emitters = emitterRepository
                .findAllSseEmitterByMemberId(String.valueOf(memberId));
        emitters.forEach(sseEmitter -> {
            sendMessage(sseEmitter, content);
        });
    }

    @Scheduled(fixedRate = 10000)
    public void sendKeepAlive() {
        log.info("Sending keep-alive to all SseEmitter");

        emitterRepository.findAll().forEach((emitter) -> {
            sendMessage(emitter, KEEP_ALIVE);
        });
    }

    private void sendMessage(SseEmitter sseEmitter, String data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .data(data));
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
            if(e.getMessage().contains("Broken pipe")){
                log.error("Broken pipe: {}", e.getMessage());
            }
        }
    }

}
