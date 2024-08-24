package com.example.travelday.domain.supersale.controller;

import com.amadeus.exceptions.ResponseException;
import com.example.travelday.domain.supersale.dto.request.FlightReqDto;
import com.example.travelday.domain.supersale.dto.response.FlightResDto;
import com.example.travelday.domain.supersale.service.FlightService;
import com.example.travelday.global.common.ApiResponseEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    /**
     * 항공 조회 API
     */
    @GetMapping()
    public ResponseEntity<ApiResponseEntity<Object>> getFlightOffers(@RequestParam(required=true) String origin,
                                                        @RequestParam(required=true) String destination,
                                                        @RequestParam(required=true) String departDate,
                                                        @RequestParam(required=true) String adults,
                                                        @RequestParam(required = false) String returnDate)
        throws ResponseException {
            log.info("origin: {}, destination: {}, departDate: {}, adults: {}, returnDate: {}", origin, destination, departDate, adults, returnDate);
            return ResponseEntity.ok(ApiResponseEntity.of(flightService.getFlightOffers(origin, destination, departDate, adults, returnDate)));
    }

    /**
     * 최저가 항공 상세 조회
     */
    @GetMapping("/lowest-price")
    public ResponseEntity<ApiResponseEntity<FlightResDto>> getLowestPriceFlights(@RequestBody FlightReqDto flightReqDto) {
        return ResponseEntity.ok(ApiResponseEntity.of(flightService.getLowestPriceFlights(flightReqDto)));
    }
}


