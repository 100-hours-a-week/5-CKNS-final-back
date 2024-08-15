package com.example.travelday.domain.flight.controller;

import com.amadeus.exceptions.ResponseException;
import com.example.travelday.domain.flight.service.FlightService;
import com.example.travelday.global.common.ApiResponseEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/")
    public String index() {
        return "Hello, World!";
    }

    @GetMapping("/flights")
    public ResponseEntity<ApiResponseEntity<Object>> getFlightOffers(@RequestParam(required=true) String origin,
                                                        @RequestParam(required=true) String destination,
                                                        @RequestParam(required=true) String departDate,
                                                        @RequestParam(required=true) String adults,
                                                        @RequestParam(required = false) String returnDate)
        throws ResponseException {
            log.info("origin: {}, destination: {}, departDate: {}, adults: {}, returnDate: {}", origin, destination, departDate, adults, returnDate);
            return ResponseEntity.ok(ApiResponseEntity.of(flightService.getFlightOffers(origin, destination, departDate, adults, returnDate)));
    }
}


