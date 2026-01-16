package com.myce.notification.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpoStartRequest {
    List<Long> userIds;
    String expoTitle;
    Long expoId;
}
