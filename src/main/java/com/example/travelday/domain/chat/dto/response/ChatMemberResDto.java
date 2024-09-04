package com.example.travelday.domain.chat.dto.response;

import com.example.travelday.domain.auth.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMemberResDto(
    String id,
    String nickname,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {
    public static ChatMemberResDto of(Member member) {
        return ChatMemberResDto
                    .builder()
                    .id(member.getUserId())
                    .nickname(member.getNickname())
                    .createdAt(LocalDateTime.now())
                    .build();
    }
}
