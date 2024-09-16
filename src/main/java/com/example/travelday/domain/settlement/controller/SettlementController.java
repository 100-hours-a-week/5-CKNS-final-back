package com.example.travelday.domain.settlement.controller;

import com.example.travelday.domain.settlement.dto.response.SettlementDetailResDto;
import com.example.travelday.domain.settlement.dto.response.SettlementResDto;
import com.example.travelday.domain.settlement.service.SettlementService;
import com.example.travelday.global.common.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("컨트롤러 진입, travelRoomId: {}, username: {}", travelRoomId, userDetails.getUsername());
        SettlementResDto settlement = settlementService.getAllSettlement(travelRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(settlement));
    }

    /**
     * 정산 상세 내역 조회
     */
    @GetMapping("/{travelRoomId}/{settlement}/detail")
    public ResponseEntity<ApiResponseEntity<List<SettlementDetailResDto>>> getSettlementDetailList (@PathVariable Long travelRoomId, @PathVariable Long settlement, @AuthenticationPrincipal UserDetails userDetails) {
        List<SettlementDetailResDto> settlements = settlementService.getSettlementDetailList(travelRoomId, settlement, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(settlements));
    }
}
