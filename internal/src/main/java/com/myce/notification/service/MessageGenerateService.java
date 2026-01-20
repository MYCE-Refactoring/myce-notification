package com.myce.notification.service;

import com.myce.notification.dto.request.message.ReservationConfirmRequest;
import com.myce.notification.dto.request.message.ResetRequest;
import com.myce.notification.dto.request.message.VerificationRequest;

public interface MessageGenerateService {
    void getMessageForVerification(VerificationRequest req);

    void getMessageForResetPassword(ResetRequest req);

    void getMessageForReservationConfirmation(ReservationConfirmRequest req);

}
