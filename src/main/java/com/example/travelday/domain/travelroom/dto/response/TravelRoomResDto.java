package com.example.travelday.domain.travelroom.dto.response;

import com.example.travelday.domain.travelroom.entity.TravelRoom;
import lombok.Builder;

@Builder
public record TravelRoomResDto(
    Long id,
    String name,
    String startDate,
    String endDate
) {
    public static TravelRoomResDto fromEntity(TravelRoom travelRoom) {
        return TravelRoomResDto.builder()
                    .id(travelRoom.getId())
                    .name(travelRoom.getName())
                    .startDate(travelRoom.getStartDate())
                    .endDate(travelRoom.getEndDate())
                    .build();
    }
}