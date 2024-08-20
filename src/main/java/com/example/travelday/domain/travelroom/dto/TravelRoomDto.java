package com.example.travelday.domain.travelroom.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TravelRoomDto {
    private String roomId;
    private String roomName;

    public static TravelRoomDto create(String room_name) {
        TravelRoomDto travelRoomDto = new TravelRoomDto();
        travelRoomDto.roomId = UUID.randomUUID().toString();
        travelRoomDto.roomName = room_name;
        return travelRoomDto;
    }
}
