package com.myce.notification.client.feign;

import com.myce.notification.client.config.FeignConfig;
import com.myce.notification.dto.email.EnsureExpoPermissionRequest;
import com.myce.notification.dto.email.MailSendContextRequest;
import com.myce.notification.dto.email.MailSendContextResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "core-internal",
        configuration = FeignConfig.class
)

public interface ExternalFeignClient {

    @PostMapping("/internal/notifications/ensure/viewable")
    void ensureViewable(@RequestBody EnsureExpoPermissionRequest request);

    @PostMapping("/internal/notifications/ensure/editable")
    void ensureEditable(@RequestBody EnsureExpoPermissionRequest request);

    @PostMapping("/internal/notifications/mail-context")
    MailSendContextResponse mailContext(@RequestBody MailSendContextRequest request);
}

