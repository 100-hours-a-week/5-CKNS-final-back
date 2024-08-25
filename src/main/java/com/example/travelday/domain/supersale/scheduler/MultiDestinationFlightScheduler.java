package com.example.travelday.domain.supersale.scheduler;

import com.amadeus.exceptions.ResponseException;
import com.example.travelday.domain.supersale.service.FlightService;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultiDestinationFlightScheduler {

    private final FlightService flightService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void fetchAndStoreFlightOffers() {

        log.info("===== fetchAndStoreFlightOffers for Multiple Destinations =====");

        String originICN = "ICN";
        String destination = "ICN";
        String adults = "1";

        // 대상 목적지 목록
        String[] airportList = {"PQC", "OIT", "CNX", "TPE", "NRT",
            "DPS", "OKA", "FUK", "JFK", "NGO",
            "CDG", "KIX", "LGA", "SYD", "MAD",
            "LHR", "VIE", "CDG", "FRA", "FCO"
        };

        // 현재 날짜 기준 하루 뒤 편도
        LocalDate departureDate = LocalDate.now().plusDays(2);
        String departDate = departureDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // 각 목적지에 대해 검색 및 저장
        // ICN에서 출발하는 각 공항으로 검색 및 저장
        for (String airport : airportList) {
            try {
                flightService.getFlightOffers(originICN, airport, departDate, adults);
                flightService.getFlightOffers(airport, originICN, departDate, adults);
                log.info("Successfully fetched and stored flight data for destination from ICN to: {}", destination);
            } catch (ResponseException e) {
                log.error("Failed to fetch flight data from ICN to destination: {} - {}", destination, e.getMessage());
                throw new CustomException(ErrorCode.FAIL_TO_GET_FLIGHT_INFO);
            }
        }
    }
}