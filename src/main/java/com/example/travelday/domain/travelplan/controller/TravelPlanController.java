package com.example.travelday.domain.travelplan.controller;

import com.example.travelday.domain.travelplan.dto.request.group.UpdateTravelPlansReqDto;
import com.example.travelday.domain.travelplan.dto.request.group.TravelPlansReqDto;
import com.example.travelday.domain.travelplan.dto.request.TravelPlanReqDto;
import com.example.travelday.domain.travelplan.dto.response.TravelPlanResDto;
import com.example.travelday.domain.travelplan.service.TravelPlanService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rooms/{travelRoomId}/plan")
@RequiredArgsConstructor
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    /**
     * 여행 일정 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponseEntity<List<TravelPlanResDto>>> getTravelPlanList(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        List<TravelPlanResDto> travelPlans = travelPlanService.getAllTravelPlan(travelRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(travelPlans));
    }

    /**
     * 수정 중인지 확인
     */
    @PostMapping("/check/editable")
    public ResponseEntity<ApiResponseEntity<Boolean>> isEditable(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponseEntity.of(travelPlanService.checkUsing(travelRoomId, userDetails.getUsername())));
    }

    /**
     * 여행 일정 수정
     */
    @PostMapping
    public ResponseEntity<ApiResponseEntity<String>> updateTravelPlan(@PathVariable Long travelRoomId, @RequestBody @Valid UpdateTravelPlansReqDto updateTravelPlanListDto, @AuthenticationPrincipal UserDetails userDetails) {
        travelPlanService.updateTravelPlan(travelRoomId, updateTravelPlanListDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_UPDATE_TRAVELPLAN));
    }

    /**
     * 여행 일정 목록 추가하기
     */
    @PostMapping("/list")
    public ResponseEntity<ApiResponseEntity<String>> addTravelPlanlist(@PathVariable Long travelRoomId, @RequestBody @Valid TravelPlansReqDto travelPlanListReqDto, @AuthenticationPrincipal UserDetails userDetails) {
        travelPlanService.addTravelPlanList(travelRoomId, travelPlanListReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_TRAVELPLANLIST));
    }

    /**
     * 여행 일정 바로 추가
     */
    @PostMapping("/direct")
    public ResponseEntity<ApiResponseEntity<String>> addTravelPlanDirect(@PathVariable Long travelRoomId, @RequestBody TravelPlanReqDto travelPlanReqDto, @AuthenticationPrincipal UserDetails userDetails) {
        travelPlanService.addTravelPlanDirect(travelRoomId, travelPlanReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_TRAVELPLAN));
    }

    /**
     * 여행 일정 삭제하기
     */
    @DeleteMapping("/{travelPlanId}")
    public ResponseEntity<ApiResponseEntity<String>> deleteTravelPlan(@PathVariable Long travelRoomId, @PathVariable Long travelPlanId, @AuthenticationPrincipal UserDetails userDetails) {
        travelPlanService.deleteTravelPlan(travelRoomId, travelPlanId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_DELETE_TRAVELPLAN));
    }
}
