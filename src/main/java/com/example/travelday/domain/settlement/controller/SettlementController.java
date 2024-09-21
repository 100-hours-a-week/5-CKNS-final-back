package com.example.travelday.domain.settlement.controller;

import com.example.travelday.domain.settlement.dto.request.SettlementDetailReqDto;
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
     * 정산 조회
     */
    @GetMapping("/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<SettlementResDto>> getSettlement(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        SettlementResDto settlement = settlementService.getAllSettlement(travelRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(settlement));
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
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_ADD_SETTLEMENT));
    }

    /**
     * 정산 내역 수정하기
     */
    @PatchMapping("/{travelRoomId}/{settlementId}/{settlementDetailId}")
    public ResponseEntity<ApiResponseEntity<String>> updateSettlement(@PathVariable Long travelRoomId, @PathVariable Long settlementId,
                                                                      @PathVariable Long settlementDetailId, @RequestBody @Valid SettlementDetailReqDto settlementDetailReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.updateSettlementDetail(travelRoomId, settlementId, settlementDetailId, settlementDetailReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_UPDATE_SETTLEMENT));
    }

    /**
     * 정산 내역 삭제하기
     */
    @DeleteMapping("/{travelRoomId}/{settlementId}/{settlementDetailId}")
    public ResponseEntity<ApiResponseEntity<String>> deleteSettlement(@PathVariable Long travelRoomId, @PathVariable Long settlementId,
                                                                      @PathVariable Long settlementDetailId, @AuthenticationPrincipal UserDetails userDetails) {
        settlementService.deleteSettlementDetail(travelRoomId, settlementId, settlementDetailId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_DELETE_SETTLEMENT));
    }
}
