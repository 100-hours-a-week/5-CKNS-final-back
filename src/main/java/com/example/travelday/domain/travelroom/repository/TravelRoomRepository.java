package com.example.travelday.domain.travelroom.repository;

import com.example.travelday.domain.travelroom.dto.TravelRoomDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TravelRoomRepository {

    private Map<String, TravelRoomDto> travelRoomMap;

    @PostConstruct
    private void init() {
        travelRoomMap = new LinkedHashMap<>();
    }

    public List<TravelRoomDto> findAllRoom() {
        // 채팅방 생성순서 최근 순으로 반환
        List TravelRooms = new ArrayList<>(travelRoomMap.values());
        Collections.reverse(TravelRooms);
        return TravelRooms;
    }

    public TravelRoomDto findRoomById(String id) {
        return travelRoomMap.get(id);
    }

    public TravelRoomDto createTravelRoom(String name) {
        TravelRoomDto travelRoom = TravelRoomDto.create(name);
        travelRoomMap.put(travelRoom.getRoomId(), travelRoom);
        return travelRoom;
    }

    public void deleteRoom(String id) {
        travelRoomMap.remove(id);
    }
}
