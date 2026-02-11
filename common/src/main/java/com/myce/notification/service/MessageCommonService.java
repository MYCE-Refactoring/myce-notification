package com.myce.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myce.notification.dto.request.SendGetMessageRequest;
import com.myce.notification.entity.type.MessageTemplateCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageCommonService {

    private final TemplateEngine templateEngine;
    private final ObjectMapper objectMapper;

    public String getFullMessage(SendGetMessageRequest req) {

        Map<String, String> templateData = parseJsonContent(req.getContent());

        Context context = req.getContext();
        // JSON 데이터를 먼저 설정하고, 동적 데이터가 덮어쓰도록 함
        for (Map.Entry<String, String> entry : templateData.entrySet()) {
            if (!context.containsVariable(entry.getKey())) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        String target = getTargetFile(req.getCode(), req.getIsUseImage());
        return templateEngine.process(target,context);
    }

    private String getTargetFile(MessageTemplateCode code, boolean isUseImage) {

        if (code.equals(MessageTemplateCode.RESET_PASSWORD)) {
            return "mail/mail-password";
        }

        if (code.equals(MessageTemplateCode.RESERVATION_CONFIRM)) {
            return "mail/mail-reservation";
        }

        return isUseImage
                ? "mail/mail-image"
                : "mail/mail-code";
    }


    private Map<String, String> parseJsonContent(String jsonContent) {
        try {
            return objectMapper.readValue(jsonContent, Map.class);
        } catch (JsonProcessingException je) {
            throw new IllegalArgumentException("JSON 파싱 실패: " + je.getMessage(), je);
        }
    }
}
