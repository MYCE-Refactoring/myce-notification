package com.myce.notification.service;

import com.myce.notification.dto.MessageFormat;
import com.myce.notification.entity.type.MessageTemplateCode;

import java.util.Map;

public interface MessageTemplateService {

    MessageFormat renderMessage(MessageTemplateCode templateCode, Map<String, String> variables);
}
