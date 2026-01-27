package com.myce.notification.service.impl;

import com.myce.notification.dto.MessageFormat;
import com.myce.notification.entity.MessageTemplateSetting;
import com.myce.notification.entity.type.ChannelType;
import com.myce.notification.entity.type.MessageTemplateCode;
import com.myce.notification.exception.CustomErrorCode;
import com.myce.notification.exception.CustomException;
import com.myce.notification.repository.MessageTemplateSettingRepository;
import com.myce.notification.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateSettingRepository messageTemplateSettingRepository;

    @Override
    public MessageFormat renderMessage(MessageTemplateCode templateCode, Map<String, String> variables) {

        MessageTemplateSetting template = messageTemplateSettingRepository.findByCodeAndChannelType(
                        templateCode, ChannelType.NOTIFICATION)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_EXIST_MESSAGE_TEMPLATE));

        String content = template.getContent();

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            content = content.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return new MessageFormat(template.getSubject(), content);
    }
}
