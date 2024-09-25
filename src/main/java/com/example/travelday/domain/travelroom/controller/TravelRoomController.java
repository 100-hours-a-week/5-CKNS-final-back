package com.example.travelday.domain.travelroom.controller;

import com.example.travelday.domain.auth.dto.response.MemberInfoResDto;
import com.example.travelday.domain.travelroom.dto.request.TravelRoomReqDto;
import com.example.travelday.domain.travelroom.dto.response.TravelRoomMembersResDto;
import com.example.travelday.domain.travelroom.dto.response.TravelRoomResDto;
import com.example.travelday.domain.travelroom.service.TravelRoomService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ApiResponseEntity<List<TravelRoomMembersResDto>>> getAllTravelRoom(@AuthenticationPrincipal UserDetails userDetails) {
        List<TravelRoomMembersResDto> travelRooms = travelRoomService.getAllTravelRoom(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(travelRooms));
    }

    /**
     * 여행방 단일 조회
     */
    @GetMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<TravelRoomResDto>> getTravelRoom(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        TravelRoomResDto travelRoom = travelRoomService.getTravelRoomById(travelRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(travelRoom));
    }

    /**
     * 여행방 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponseEntity<String>> createTravelRoom(@RequestBody TravelRoomReqDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        travelRoomService.createTravelRoom(requestDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_TRAVELROOM));
    }

    /**
     * 여행방 삭제
     */
    @DeleteMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<String>> deleteTravelRoom(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        travelRoomService.deleteTravelRoom(travelRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_DELETE_TRAVELROOM));
    }

    /**
     * 여행방 수정
     */
    @PostMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<String>> updateTravelRoom(@PathVariable Long travelRoomId, @RequestBody TravelRoomReqDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        travelRoomService.updateTravelRoom(travelRoomId, requestDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_UPDATE_TRAVELROOM));
    }

    /**
     * 초대할 수 있는 사용자 검색
     */
    @GetMapping("/{travelRoomId}/user/search")
    public ResponseEntity<ApiResponseEntity<List<MemberInfoResDto>>> searchMembersToInvite(@PathVariable Long travelRoomId, @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponseEntity.of(travelRoomService.searchMembersToInvite(travelRoomId, keyword)));
    }

    /**
     * 여행방 내 사용자 조회
     */
    @GetMapping("/{travelRoomId}/user")
    public ResponseEntity<ApiResponseEntity<List<MemberInfoResDto>>> getMebmersInTravelRoom(@PathVariable Long travelRoomId) {
        return ResponseEntity.ok(ApiResponseEntity.of(travelRoomService.getMembersInTravelRoom(travelRoomId)));
    }
}
