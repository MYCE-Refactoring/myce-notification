package com.myce.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompleteRequest {
    private Long memberId;
    private Long reservationId;
    private String expoTitle;
    private String paymentAmount;
}

//tsetsetset