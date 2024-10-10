package com.example.travelday.domain.chat.dto.response;

import com.example.travelday.domain.chat.entity.Chat;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record ChatMessageResDto(
    String id,
    Long travelRoomId,
    String senderNickname,
    String message,
    String senderId,
    String createdAt,
    long leftBucketToken
) {
    // TODO: 추후 유저 프로필이 추가 후 UserDetail Custom 을 통해 가져오기.senderProfileImage(chat.getSenderProfileImage())
    public static ChatMessageResDto of(Chat chat, long leftBucketToken) {
        return ChatMessageResDto.builder()
                    .id(chat.getId())
                    .travelRoomId(chat.getTravelRoomId())
                    .senderNickname(chat.getSenderNickname())
                    .message(chat.getMessage())
                    .senderId(chat.getSenderId())
                    .createdAt(chat.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .leftBucketToken(leftBucketToken)
                    .build();
    }
}
