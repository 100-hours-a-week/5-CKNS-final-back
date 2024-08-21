package com.example.travelday.domain.travelroom.dto.request;

import lombok.Builder;

@Builder
public record TravelRoomCreateReqDto(
    String name,
    String userId
) {
}
