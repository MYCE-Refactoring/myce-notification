package com.myce.notification.dto;

import com.myce.notification.document.type.AdvertisementStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdStatusChangeCommand {
        Long memberId;
        Long adId;
        String adTitle;
        AdvertisementStatus oldStatus;
        AdvertisementStatus newStatus;
}
