package com.example.travelday.domain.travelroom.dto.response;

import com.example.travelday.domain.travelroom.entity.Chat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatResDto(
    String id,
    Long travelRoomId,
    String senderId,
    String message,
    String senderProfileImage,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {
    public static ChatResDto of(Chat chat, String userId) {
        return ChatResDto.builder()
                .id(chat.getId().toHexString())
                .travelRoomId(chat.getTravelRoomId())
                .senderId(userId)
                .message(chat.getMessage())
// TODO: 추후 유저 프로필이 추가 후 UserDetail Coustom 을 통해 가져오기
//              .senderProfileImage(chat.getSenderProfileImage())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
