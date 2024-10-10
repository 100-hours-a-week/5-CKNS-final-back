package com.example.travelday.domain.chat.dto.response;

import com.example.travelday.domain.chat.entity.Chat;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record ChatResDto(
    String id,
    Long travelRoomId,
    String senderNickname,
    String message,
    String senderId,
    String createdAt
) {
    // TODO: 추후 유저 프로필이 추가 후 UserDetail Custom 을 통해 가져오기.senderProfileImage(chat.getSenderProfileImage())
    public static ChatResDto of(Chat chat) {
        return ChatResDto.builder()
                .id(chat.getId())
                .travelRoomId(chat.getTravelRoomId())
                .senderId(chat.getSenderId())
                .senderNickname(chat.getSenderNickname())
                .message(chat.getMessage())
                .createdAt(chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
