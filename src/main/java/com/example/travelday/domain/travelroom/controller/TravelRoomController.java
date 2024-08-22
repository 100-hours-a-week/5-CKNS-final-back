package com.example.travelday.domain.travelroom.controller;

import com.example.travelday.domain.travelroom.dto.request.TravelRoomCreateReqDto;
import com.example.travelday.domain.travelroom.dto.response.TravelRoomResDto;
import com.example.travelday.domain.travelroom.service.TravelRoomService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class TravelRoomController {

    private final TravelRoomService travelRoomService;

    /**
     * 여행방 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponseEntity<List<TravelRoomResDto>>> getAllTravelRoom() {
        List<TravelRoomResDto> travelRooms = travelRoomService.getAllTravelRoom();
        return ResponseEntity.ok(ApiResponseEntity.of(travelRooms));
    }

    /**
     * 여행방 단일 조회
     */
    @GetMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<TravelRoomResDto>> getTravelRoom(@PathVariable Long travelRoomId) {
        TravelRoomResDto travelRoom = travelRoomService.getTravelRoomById(travelRoomId);
        return ResponseEntity.ok(ApiResponseEntity.of(travelRoom));
    }

    /**
     * 여행방 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponseEntity<String>> createTravelRoom(@RequestBody TravelRoomCreateReqDto requestDto) {
        travelRoomService.createTravelRoom(requestDto);
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_TRAVELROOM));
    }

    /**
     * 여행방 삭제
     */
    @DeleteMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<Void>> deleteTravelRoom(@PathVariable Long travelRoomId) {
        travelRoomService.deleteTravelRoom(travelRoomId);
        return ResponseEntity.ok(ApiResponseEntity.of(null));
    }
}
