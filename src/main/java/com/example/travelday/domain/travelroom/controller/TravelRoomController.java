package com.example.travelday.domain.travelroom.controller;

import com.example.travelday.domain.travelroom.dto.TravelRoomDto;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class TravelRoomController {

    private final TravelRoomRepository travelRoomRepository;

    /**
     * 여행 방 목록 조회
     */
    @GetMapping()
    public List<TravelRoomDto> room() {
        return travelRoomRepository.findAllRoom();
    }

    /**
    * 여행 방 단일 조회
     */
    @GetMapping("/{travel_room_id}")
    public TravelRoomDto roomInfo(@PathVariable String travel_room_id) {
        return travelRoomRepository.findRoomById(travel_room_id);
    }

    /**
     * 여행 방 생성
     */
    @PostMapping()
    public TravelRoomDto createRoom(@RequestParam String name) {
        return travelRoomRepository.createTravelRoom(name);
    }

    /**
     * 여행 방 삭제
     */
    @DeleteMapping("/{travel_room_id}")
    public void deleteRoom(@PathVariable String travel_room_id) {
        travelRoomRepository.deleteRoom(travel_room_id);
    }

}