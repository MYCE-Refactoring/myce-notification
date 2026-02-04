package com.myce.notification.service.internal;

import com.myce.notification.dto.MessageFormat;
import com.myce.notification.entity.type.MessageTemplateCode;

import java.util.Map;

public interface MessageRenderService {

    MessageFormat renderMessage(MessageTemplateCode templateCode, Map<String, String> variables);
}
