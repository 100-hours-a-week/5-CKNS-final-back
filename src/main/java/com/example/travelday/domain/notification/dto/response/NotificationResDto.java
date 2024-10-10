package com.example.travelday.domain.notification.dto.response;

import com.example.travelday.domain.notification.entity.Notification;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record NotificationResDto(
        Long notificationId,
        String content,
        String notificationTime,
        boolean isChecked,
        Long travelRoomId,
        Long invitationId
) {

    public static NotificationResDto of(Notification notification, Long invitationId) {
        return NotificationResDto.builder()
                .notificationId(notification.getId())
                .content(notification.getContent())
                .notificationTime(notification.getCreatedTime().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")))
                .isChecked(notification.isChecked())
                .travelRoomId(notification.getTravelRoomId())
                .invitationId(invitationId)
                .build();
    }
}
