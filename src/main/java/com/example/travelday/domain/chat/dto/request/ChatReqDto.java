package com.example.travelday.domain.chat.dto.request;

import lombok.Builder;

@Builder
public record ChatReqDto(
        String message,
        String senderId
) {
}
