package com.myce.notification.service;

import com.myce.notification.dto.*;
import com.myce.notification.dto.request.EventReminderRequest;
import com.myce.notification.dto.request.ExpoStartRequest;
import com.myce.notification.dto.request.PaymentCompleteRequest;
import com.myce.notification.dto.request.QrIssuedRequest;

public interface NotificationInternalService {

    void sendQrIssuedNotification(QrIssuedRequest req);
    void sendExpoStartNotification(ExpoStartRequest req);
    void sendEventHourReminderNotification(EventReminderRequest req);
    void sendPaymentCompleteNotification(PaymentCompleteRequest req);
    void sendExpoStatusChangeNotification(ExpoStatusChangeCommand command);
    void sendAdvertisementStatusChangeNotification(AdStatusChangeCommand command);
}
