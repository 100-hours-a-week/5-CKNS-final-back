package com.example.travelday.domain.supersale.controller;

import com.example.travelday.domain.supersale.dto.request.FlightReqDto;
import com.example.travelday.domain.supersale.dto.response.FlightResDto;
import com.example.travelday.domain.supersale.service.FlightService;
import com.example.travelday.global.common.ApiResponseEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    /**
     * 최저가 항공 목록 조회
     */
    @GetMapping("/lowestPrice/list")
    public ResponseEntity<ApiResponseEntity<List<FlightResDto>>> getLowestPriceFlights() {
        return ResponseEntity.ok(ApiResponseEntity.of(flightService.getLowestPriceFlights()));
    }

    /**
     * 최저가 항공 상세 조회
     */
    @GetMapping("/lowestPrice")
    public ResponseEntity<ApiResponseEntity<FlightResDto>> getLowestPriceFlight(@RequestBody FlightReqDto flightReqDto) {
        return ResponseEntity.ok(ApiResponseEntity.of(flightService.getLowestPriceFlight(flightReqDto)));
    }
}


