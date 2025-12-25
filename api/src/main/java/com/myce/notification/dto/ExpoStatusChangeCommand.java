package com.myce.notification.dto;

import com.myce.notification.document.type.ExpoStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpoStatusChangeCommand {
        Long memberId;
        Long expoId;
        String expoTitle;
        ExpoStatus oldStatus;
        ExpoStatus newStatus;
}
