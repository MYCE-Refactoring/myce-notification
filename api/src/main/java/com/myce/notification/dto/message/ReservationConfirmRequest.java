package com.myce.notification.dto.message;

import com.myce.notification.entity.type.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationConfirmRequest {
    String email;
    String name;
    String expoTitle;
    String reservationCode;
    Integer quantity;
    String paymentAmount;
    UserType userType;
}
