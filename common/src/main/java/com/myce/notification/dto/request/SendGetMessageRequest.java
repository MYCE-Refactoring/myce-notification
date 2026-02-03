package com.myce.notification.dto.request;

import com.myce.notification.entity.type.MessageTemplateCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.thymeleaf.context.Context;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendGetMessageRequest {
    String content;
    MessageTemplateCode code;
    Boolean isUseImage;
    Context context;
    //123
}
