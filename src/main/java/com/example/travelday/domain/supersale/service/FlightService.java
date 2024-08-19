package com.example.travelday.domain.supersale.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.example.travelday.domain.supersale.dto.response.FlightResDto;
import com.example.travelday.domain.supersale.entity.AmadeusConnect;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final AmadeusConnect amadeusConnect;

    private final ValueOperations<String, List<Object>> valueOperations;

    @Value("${spring.data.redis.timeout}")
    private long redisTTL;

    public List<FlightResDto> getFlightOffers(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        try {
            String redisKey = "flightOffer:" + origin + ":" + destination + ":" + departDate + ":" + returnDate + ":" + adults;

            //  Redis에서 데이터 조회
            List<Object> cachedData = valueOperations.get(redisKey);
            if (cachedData != null) {
                List<FlightResDto> flightResDtos = new ArrayList<>();
                for (Object flight : cachedData) {
                    flightResDtos.add((FlightResDto) flight);
                }
                return flightResDtos;
            }

            // Redis 데이터가 없거나 문제가 있을 경우 Amadeus API 호출
            List<FlightOfferSearch> flightOffers = List.of(amadeusConnect.flights(origin, destination, departDate, adults, returnDate));
            List<FlightResDto> flightResDtos = new ArrayList<>();

            for (FlightOfferSearch flight : flightOffers) {
                flightResDtos.add(FlightResDto.of(flight));
            }

            // Redis에 데이터 저장 (TTL 설정)
            valueOperations.set(redisKey, Collections.singletonList(flightResDtos), Duration.ofSeconds(redisTTL));

            return flightResDtos;
        } catch (ResponseException e) {
            log.info(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_GET_FLIGHT_INFO);
        }
    }
}