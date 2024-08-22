package com.example.travelday.domain.travelroom.controller;

import com.example.travelday.domain.travelroom.dto.request.TravelPlanListOverwriteDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanListReqDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanReqDto;
import com.example.travelday.domain.travelroom.dto.response.TravelPlanResDto;
import com.example.travelday.domain.travelroom.service.TravelPlanService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponseEntity<List<TravelPlanResDto>>> getTravelPlanList(@PathVariable Long travelRoomId) {
        List<TravelPlanResDto> travelPlans = travelPlanService.getAllTravelPlan(travelRoomId);
        return ResponseEntity.ok(ApiResponseEntity.of(travelPlans));
    }

    /**
     * 여행 일정 덮어쓰기
     */
    @PostMapping("/overwrite")
    public ResponseEntity<ApiResponseEntity<String>> updateTravelPlan(@PathVariable Long travelRoomId, @RequestBody @Valid TravelPlanListOverwriteDto travelPlanListOverwriteDto) {
        log.info("여행 일정 덮어쓰기 tarvelPlanList = {}, travelRoomId = {}", travelPlanListOverwriteDto, travelRoomId);
        travelPlanService.updateTravelPlan(travelRoomId, travelPlanListOverwriteDto);
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_UPDATE_TRAVELPLAN));
    }

    /**
     * 여행 일정 목록 추가하기
     */
    @PostMapping("/list")
    public ResponseEntity<ApiResponseEntity<String>> addTravelPlanlist(@PathVariable Long travelRoomId, @RequestBody @Valid TravelPlanListReqDto travelPlanListReqDto) {
        travelPlanService.addTravelPlanList(travelRoomId, travelPlanListReqDto);
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_TRAVELPLANLIST));
    }

    /**
     * 여행 일정 바로 추가
     */
    @PostMapping("/direct")
    public ResponseEntity<ApiResponseEntity<String>> addTravelPlanDirect(@PathVariable Long travelRoomId, @RequestBody TravelPlanReqDto travelPlanReqDto) {
        travelPlanService.addTravelPlanDirect(travelRoomId, travelPlanReqDto);
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_TRAVELPLAN));
    }

    /**
     * 여행 일정 삭제하기
     */
    @DeleteMapping("/{travelPlanId}")
    public ResponseEntity<ApiResponseEntity<Void>> deleteTravelPlan(@PathVariable Long travelRoomId, @PathVariable Long travelPlanId) {
        travelPlanService.deleteTravelPlan(travelRoomId, travelPlanId);
        return ResponseEntity.ok(ApiResponseEntity.of(null));
    }


}
