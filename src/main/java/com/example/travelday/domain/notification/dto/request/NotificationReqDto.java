package com.example.travelday.domain.notification.dto.request;

import lombok.Builder;

@Builder
public record NotificationReqDto(
        String userId,

        String notificationContent,
        Long travelRoomId

) {
}
