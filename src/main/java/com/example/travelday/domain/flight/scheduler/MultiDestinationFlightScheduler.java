package com.example.travelday.domain.flight.scheduler;

import com.amadeus.exceptions.ResponseException;
import com.example.travelday.domain.flight.service.FlightService;
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

    /**
     * 인천 -> 여러 목적지 2박 3일 최저가
     */
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void fetchAndStoreFlightOffers() {
        log.info("===== fetchAndStoreFlightOffers for Multiple Destinations =====");

        String origin = "ICN"; // 인천공항
        String adults = "1";

        // 대상 목적지 목록
        String[] destinations = {"KIX", "FUK", "NRT", "HND", "OKA", "GUM", "BKK", "TPE", "DAD"};

        // 현재 날짜 기준 2일 후 3박 4일
        LocalDate departureDate = LocalDate.now().plusDays(2);
        LocalDate returnDate = departureDate.plusDays(4);

        String departDate = departureDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String returnDateStr = returnDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // 각 목적지에 대해 검색 및 저장
        for (String destination : destinations) {
            try {
                flightService.getFlightOffers(origin, destination, departDate, adults, returnDateStr);
                log.info("Successfully fetched and stored flight data for destination: {}", destination);
            } catch (ResponseException e) {
                log.error("Failed to fetch flight data for destination: {} - {}", destination, e.getMessage());
            }
        }
    }
}