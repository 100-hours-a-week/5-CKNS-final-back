package com.example.travelday.domain.supersale.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.example.travelday.domain.supersale.dto.request.FlightReqDto;
import com.example.travelday.domain.supersale.dto.response.FlightResDto;
import com.example.travelday.domain.supersale.entity.Airport;
import com.example.travelday.domain.supersale.entity.FlightOffer;
import com.example.travelday.domain.supersale.repository.AirportRepository;
import com.example.travelday.domain.supersale.repository.FlightOfferRepository;
import com.example.travelday.domain.supersale.repository.RawFlightOfferRepository;
import com.example.travelday.domain.supersale.utils.AmadeusConnect;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.example.travelday.global.utils.BucketUtils;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final AmadeusConnect amadeusConnect;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson = new Gson();
    private final FlightOfferRepository flightOfferRepository;
    private final AirportRepository airportRepository;
    private final BucketUtils bucketUtils;
    private final RawFlightOfferRepository rawFlightOfferRepository;

    @Value("${spring.data.redis.timeout}")
    private long redisTTL;

    public void getFlightOffers(String origin, String destination, String departDate, String adults) throws ResponseException {

        try {
            String redisKey = "flightOffer:" + origin + ":"+ destination + ":" + departDate;

            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

            // Redis에서 데이터가 이미 존재하는지 확인
            String cachedFlightData = (String) valueOperations.get(redisKey);
            if (cachedFlightData != null) {
                log.info("Flight data for destination from {} to {} on {} is already cached. Skipping API call.", origin, destination, departDate);
                return;
            }
            // amadeus api 호출
            bucketUtils.checkRequestBucketCount();
            FlightOfferSearch[] flightOffersJson = amadeusConnect.flights(origin, destination, departDate, adults);

            // flightOffersJson을 JSON 문자열로 변환
            String flightOffersJsonString = gson.toJson(flightOffersJson);

            // flightOffersJsonString을 MySQL에 저장
            FlightOffer flightOfferEntity = new FlightOffer(flightOffersJsonString);
            flightOfferRepository.save(flightOfferEntity);

            List<FlightOfferSearch> flightOffers = List.of(flightOffersJson);
            List<FlightResDto> flightResDtos = new ArrayList<>();

            for (FlightOfferSearch flight : flightOffers) {
                flightResDtos.add(FlightResDto.of(flight));
            }

            // List<FlightResDto>를 JSON 문자열로 변환 후 Redis에 저장
            try {
                String flightResDtosJson = gson.toJson(flightResDtos);
                valueOperations.set(redisKey, flightResDtosJson, Duration.ofSeconds(redisTTL));
                log.info("Successfully stored flight data for destination from {} to {} on {}", origin, destination, departDate);
            }
            catch (Exception e) {
                log.error("Failed to store flight data in Redis for destination from {} to {} on {} - {}", origin, destination, departDate, e.getMessage(), e);
                throw new CustomException(ErrorCode.REDIS_SAVE_ERROR);
            }

        } catch (ResponseException e) {
            log.info(e.getMessage());
        }
    }

    public List<FlightResDto> getLowestPriceFlights() {
        String[] destinations = {"PQC", "OIT", "CNX", "TPE", "NRT",
            "DPS", "OKA", "FUK", "JFK", "NGO",
            "CDG", "KIX", "LGA", "SYD", "MAD",
            "LHR", "VIE", "CDG", "FRA", "FCO"};
        String departDate = String.valueOf(LocalDate.now().plusDays(1));

        List<FlightResDto> flightResDtos = new ArrayList<>();
        for (String des : destinations) {
            String redisKey = "flightOffer:ICN:" + des + ":" +  departDate;

            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            String cachedDataJson = (String) valueOperations.get(redisKey);

            if (cachedDataJson != null) {
                Type listType = new TypeToken<List<FlightResDto>>() {}.getType();
                List<FlightResDto> cachedData = gson.fromJson(cachedDataJson, listType);

                if (!cachedData.isEmpty()) {
                    flightResDtos.add(cachedData.get(0));
                }
            }
        }

        return flightResDtos;
    }

    public FlightResDto getLowestPriceFlight(FlightReqDto flightReqDto) {
        String redisKey = "flightOffer:ICN:" + flightReqDto.destination() + ":" + flightReqDto.departDate();

        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String cachedDataJson = (String) valueOperations.get(redisKey);

        if (cachedDataJson == null) {
            throw new CustomException(ErrorCode.FLIGHT_NOT_FOUND);
        }

        Type listType = new TypeToken<List<FlightResDto>>() {}.getType();
        List<FlightResDto> cachedData = gson.fromJson(cachedDataJson, listType);

        return cachedData.get(0);
    }

    public List<Airport> searchAirport(String keyword) {
        List<Airport> airports = airportRepository.findByKoreanAirportNameContaining(keyword);
        airports.addAll(airportRepository.findByAirportCodeContaining(keyword));
        if (airports.isEmpty()) {
            throw new CustomException(ErrorCode.AIRPORT_NOT_FOUND);
        }
        return airports;
    }
}