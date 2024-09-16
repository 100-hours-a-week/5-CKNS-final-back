package com.example.travelday.domain.fcm.dto.request;

import lombok.Builder;

@Builder
public record FcmCreateReqDto(
        String fcmToken
) {
}
