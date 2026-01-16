package com.myce.notification.service;

import com.myce.notification.dto.message.MessageTemplate;
import com.myce.notification.entity.type.UserType;

public interface MessageGenerateService {
    MessageTemplate getMessageForVerification(String verificationName, String code, String limitTime);

    MessageTemplate getMessageForResetPassword(String password);

    MessageTemplate getMessageForReservationConfirmation(String name, String expoTitle, 
            String reservationCode, Integer quantity, String paymentAmount, UserType userType);

}
