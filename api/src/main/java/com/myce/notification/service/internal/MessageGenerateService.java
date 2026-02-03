package com.myce.notification.service.internal;

import com.myce.notification.dto.message.ReservationConfirmRequest;
import com.myce.notification.dto.message.ResetRequest;
import com.myce.notification.dto.message.VerificationRequest;

public interface MessageGenerateService {
    void getMessageForVerification(VerificationRequest req);

    void getMessageForResetPassword(ResetRequest req);

    void getMessageForReservationConfirmation(ReservationConfirmRequest req);

}
