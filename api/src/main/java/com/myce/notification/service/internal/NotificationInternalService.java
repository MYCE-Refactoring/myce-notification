package com.myce.notification.service.internal;

import com.myce.notification.dto.*;
import com.myce.notification.dto.notification.EventReminderRequest;
import com.myce.notification.dto.notification.ExpoStartRequest;
import com.myce.notification.dto.notification.PaymentCompleteRequest;
import com.myce.notification.dto.notification.QrIssuedRequest;

public interface NotificationInternalService {

    void sendQrIssuedNotification(QrIssuedRequest req);
    void sendExpoStartNotification(ExpoStartRequest req);
    void sendEventHourReminderNotification(EventReminderRequest req);
    void sendPaymentCompleteNotification(PaymentCompleteRequest req);
    void sendExpoStatusChangeNotification(ExpoStatusChangeCommand command);
    void sendAdvertisementStatusChangeNotification(AdStatusChangeCommand command);
}
