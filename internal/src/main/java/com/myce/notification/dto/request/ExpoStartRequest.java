package com.myce.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpoStartRequest {
    List<Long> userIds;
    String expoTitle;
    Long expoId;
}
