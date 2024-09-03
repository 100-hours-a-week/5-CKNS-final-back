package com.example.travelday.domain.travelroom.dto.response;

import com.example.travelday.domain.travelroom.entity.TravelRoom;
import lombok.Builder;

@Builder
public record TravelRoomMembersResDto(
    Long id,
    String name,
    String startDate,
    String endDate,
    int memberCount
) {
        public static TravelRoomMembersResDto fromEntity(TravelRoom travelRoom, int memberCount) {
            return TravelRoomMembersResDto.builder()
                        .id(travelRoom.getId())
                        .name(travelRoom.getName())
                        .startDate(travelRoom.getStartDate())
                        .endDate(travelRoom.getEndDate())
                        .memberCount(memberCount)
                        .build();
        }
}

