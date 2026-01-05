package com.myce.notification.service;

import com.myce.notification.document.type.NotificationTargetType;
import com.myce.notification.document.type.NotificationType;
import com.myce.notification.dto.QrIssuedRequest;

public interface InternalService {

    void saveNotification(Long memberId, Long targetId, String title, String content,
                          NotificationType type, NotificationTargetType targetType);
    void sendQrIssuedNotification(QrIssuedRequest req);
}
