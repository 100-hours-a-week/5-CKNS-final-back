package com.example.travelday.domain.travelroom.dto.response;

import com.example.travelday.domain.travelroom.entity.TravelRoom;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelRoomMembersResDto(
    Long id,
    String name,
    String startDate,
    String endDate,
    List<String> membersUserNames,
    int memberCount
) {
        public static TravelRoomMembersResDto fromEntity(TravelRoom travelRoom, int memberCount, List<String> membersUserNames ) {
            return TravelRoomMembersResDto.builder()
                        .id(travelRoom.getId())
                        .name(travelRoom.getName())
                        .startDate(travelRoom.getStartDate())
                        .endDate(travelRoom.getEndDate())
                        .memberCount(memberCount)
                        .membersUserNames(membersUserNames)
                        .build();
        }
}

