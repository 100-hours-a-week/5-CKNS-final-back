package com.example.travelday.domain.flight.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.example.travelday.domain.flight.enums.AmadeusConnect;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final AmadeusConnect amadeusConnect;

    private final Gson gson;

    private final ValueOperations<String, String> valueOperations;

    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.timeout}")
    private long redisTTL;

    public Object getFlightOffers(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        try {
            String redisKey = "flightOffer:" + origin + ":" + destination + ":" + departDate + ":" + returnDate + ":" + adults;

            log.info("redisKey: {}", redisKey);
            //  Redis에서 데이터 조회
            String cachedData = valueOperations.get(redisKey);
            log.info("cachedData: {}", cachedData);
            if (cachedData != null) {
                log.info("===== 캐시데이터 있졍 =======");
                return gson.fromJson(cachedData, Object.class);
            }

            log.info("===== 캐시 없졍 =======");

            // Redis 데이터가 없거나 문제가 있을 경우 Amadeus API 호출
            List<FlightOfferSearch> flightOffers = List.of(amadeusConnect.flights(origin, destination, departDate, adults, returnDate));


            // 데이터 JSON으로 변환
            String flightsToJson = gson.toJson(flightOffers);
            log.info("flightsToJson: {}", flightsToJson);

            // Redis에 데이터 저장 (TTL 설정)
            valueOperations.set(redisKey, flightsToJson, Duration.ofSeconds(redisTTL));

            return gson.fromJson(flightsToJson, Object.class);
        } catch (ResponseException e) {
            log.info(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_GET_FLIGHT_INFO);
        }
    }
}