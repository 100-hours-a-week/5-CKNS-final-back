package com.example.travelday.domain.settlement.controller;

import com.example.travelday.domain.settlement.dto.request.SettlementDetailReqDto;
import com.example.travelday.domain.settlement.dto.request.SettlementNotificationReqDto;
import com.example.travelday.domain.settlement.dto.response.SettlementDetailResDto;
import com.example.travelday.domain.settlement.dto.response.SettlementResDto;
import com.example.travelday.domain.settlement.service.SettlementService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/settlement")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    /**
     * 정산 생성
     */
    @PostMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<String>> createSettlement(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.createSettlement(travelRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_SETTLEMENT));
    }

    /**
     * 정산 조회
     */
    @GetMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<List<SettlementResDto>>> getSettlement(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        List<SettlementResDto> settlements = settlementService.getAllSettlement(travelRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(settlements));
    }

    /**
     * 정산 상세 내역 조회
     */
    @GetMapping("/{travelRoomId}/{settlement}/detail")
    public ResponseEntity<ApiResponseEntity<List<SettlementDetailResDto>>> getSettlementDetailList (@PathVariable Long travelRoomId, @PathVariable Long settlement,
                                                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        List<SettlementDetailResDto> settlements = settlementService.getSettlementDetailList(travelRoomId, settlement, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(settlements));
    }

    /**
     * 정산 내역 추가하기
     */
    @PostMapping("/{travelRoomId}/{settlementId}")
    public ResponseEntity<ApiResponseEntity<String>> addSettlement(@PathVariable Long travelRoomId, @PathVariable Long settlementId,
                                                                   @RequestBody @Valid SettlementDetailReqDto settlementDetailReqDto, @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.addSettlement(travelRoomId, settlementId, settlementDetailReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_ADD_SETTLEMENTDETAIL));
    }

    /**
     * 정산 내역 수정하기
     */
    @PatchMapping("/{travelRoomId}/{settlementId}/{settlementDetailId}")
    public ResponseEntity<ApiResponseEntity<String>> updateSettlement(@PathVariable Long travelRoomId, @PathVariable Long settlementId,
                                                                      @PathVariable Long settlementDetailId, @RequestBody @Valid SettlementDetailReqDto settlementDetailReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.updateSettlementDetail(travelRoomId, settlementId, settlementDetailId, settlementDetailReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_UPDATE_SETTLEMENTDETAIL));
    }

    /**
     * 정산 삭제하기
     */
    @DeleteMapping("/{travelRoomId}/{settlementId}")
    public ResponseEntity<ApiResponseEntity<String>> deleteSettlement(@PathVariable Long travelRoomId, @PathVariable Long settlementId, @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.deleteSettlement(travelRoomId, settlementId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_DELETE_SETTLEMENT));
    }

    /**
     * 정산 내역 삭제하기
     */
    @DeleteMapping("/{travelRoomId}/{settlementId}/{settlementDetailId}")
    public ResponseEntity<ApiResponseEntity<String>> deleteSettlement(@PathVariable Long travelRoomId, @PathVariable Long settlementId,
                                                                      @PathVariable Long settlementDetailId, @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.deleteSettlementDetail(travelRoomId, settlementId, settlementDetailId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_DELETE_SETTLEMENTDETAIL));
    }

    /**
     * 정산 알림
     */
    @PostMapping("/{travelRoomId}/notification")
    public ResponseEntity<ApiResponseEntity<String>> notifySettlement(@PathVariable Long travelRoomId, @RequestBody @Valid SettlementNotificationReqDto settlementNotificationReqDto, @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.notifySettlement(travelRoomId, settlementNotificationReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_NOTIFY_SETTLEMENTDETAIL));
    }
}
