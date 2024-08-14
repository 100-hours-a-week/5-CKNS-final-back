package com.example.travelday.domain.flight.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final AmadeusConnect amadeusConnect;
    private final ValueOperations<String, String> valueOperations;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.timeout}") // 86400초 = 24시간
    private long redisTTL;

    public FlightOfferSearch[] getFlightOffers(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        String redisKey = "flightOffer:" + origin + ":" + destination + ":" + departDate + ":" + returnDate + ":" + adults;

        // Redis에서 데이터 조회
        String cachedData = valueOperations.get(redisKey);
        if (cachedData != null) {
            try {
                return objectMapper.readValue(cachedData, FlightOfferSearch[].class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                // 캐싱된 데이터에 문제가 있으면 다시 API 호출
            }
        }

        // Redis 데이터가 없거나 문제가 있을 경우 Amadeus API 호출
        FlightOfferSearch[] flightOffers = amadeusConnect.flights(origin, destination, departDate, adults, returnDate);

        try {
            // Redis 데이터 저장 (TTL 설정)
            String jsonData = objectMapper.writeValueAsString(flightOffers);
            valueOperations.set(redisKey, jsonData, Duration.ofSeconds(redisTTL));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 직렬화 오류 처리
        }

        return flightOffers;
    }
}