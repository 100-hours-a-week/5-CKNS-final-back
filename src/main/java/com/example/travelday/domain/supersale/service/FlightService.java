package com.example.travelday.domain.supersale.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.example.travelday.domain.supersale.dto.response.FlightResDto;
import com.example.travelday.domain.supersale.utils.AmadeusConnect;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final AmadeusConnect amadeusConnect;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson = new Gson();

    @Value("${spring.data.redis.timeout}")
    private long redisTTL;

    public List<FlightResDto> getFlightOffers(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        try {
            String redisKey = "flightOffer:" + origin + ":" + destination + ":" + departDate + ":" + returnDate + ":" + adults;

            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            String cachedDataJson = (String) valueOperations.get(redisKey);
            if (cachedDataJson != null) {

                // JSON 문자열을 List<FlightResDto>로 변환
                Type listType = new TypeToken<List<FlightResDto>>() {}.getType();
                List<FlightResDto> cachedData = gson.fromJson(cachedDataJson, listType);

                return cachedData;
            }

            // Redis 데이터가 없거나 문제가 있을 경우 Amadeus API 호출
            List<FlightOfferSearch> flightOffers = List.of(amadeusConnect.flights(origin, destination, departDate, adults, returnDate));
            List<FlightResDto> flightResDtos = new ArrayList<>();

            for (FlightOfferSearch flight : flightOffers) {
                flightResDtos.add(FlightResDto.of(flight));
            }

            // List<FlightResDto>를 JSON 문자열로 변환 후 Redis에 저장
            String flightResDtosJson = gson.toJson(flightResDtos);
            valueOperations.set(redisKey, flightResDtosJson, Duration.ofSeconds(redisTTL));

            return flightResDtos;
        } catch (ResponseException e) {
            log.info(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_GET_FLIGHT_INFO);
        }
    }
}