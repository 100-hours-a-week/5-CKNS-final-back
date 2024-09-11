package com.example.travelday.domain.chat.dto.response;

import com.example.travelday.domain.chat.entity.Chat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Builder
public record ChatResDto(
    String id,
    Long travelRoomId,
    String senderId,
    String message,
    String createdAt
) {
    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    // TODO: 추후 유저 프로필이 추가 후 UserDetail Coustom 을 통해 가져오기.senderProfileImage(chat.getSenderProfileImage())
    public static ChatResDto of(Chat chat) {
        return ChatResDto.builder()
                .id(chat.getId())
                .travelRoomId(chat.getTravelRoomId())
                .senderId(chat.getSenderId())
                .message(chat.getMessage())
                .createdAt(formatToUTC(chat.getCreatedAt()))
                .build();
    }

    private static String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault())
                    .format(UTC_FORMATTER);
    }
}
