package com.example.travelday.domain.travelroom.dto.request;

import lombok.Builder;

@Builder
public record TravelRoomReqDto(
    String name,
    String startDate,
    String endDate
) {
}
