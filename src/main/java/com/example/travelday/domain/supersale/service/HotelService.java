package com.example.travelday.domain.supersale.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.HotelOfferSearch;
import com.example.travelday.domain.supersale.dto.response.HotelResDto;
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
public class HotelService {

    private final AmadeusConnect amadeusConnect;

    private final ValueOperations<String, List<Object>> valueOperations;

    @Value("${spring.data.redis.timeout}")
    private long redisTTL;

    public List<HotelResDto> getHotelsByGeocode(double latitude, double longitude) {
        try {
            String redisKey = "HotelOffer: " + latitude + "," + longitude;

            // Redis에서 데이터 조회
            List<Object> cacheData = valueOperations.get(redisKey);
            if (cacheData != null) {
                List<HotelResDto> hotelResDtos = new ArrayList<>();
                for (Object hotel : cacheData) {
                    hotelResDtos.add((HotelResDto) hotel);
                }
                log.info(cacheData.toString());
                return hotelResDtos;
            }

            List<Hotel> result = List.of(amadeusConnect.hotelsByGeocode(latitude, longitude));

            List<String> hotelIds = result.stream()
                    .map(Hotel::getHotelId)
                    .toList();

            List<HotelOfferSearch> hotelOffers = List.of(amadeusConnect.hotelOffers(hotelIds));
            List<HotelResDto> hotelResDtos = new ArrayList<>();

            for (HotelOfferSearch offer : hotelOffers) {
                hotelResDtos.add(HotelResDto.of(offer));
            }

            valueOperations.set(redisKey, Collections.singletonList(hotelResDtos), Duration.ofSeconds(redisTTL));

            return hotelResDtos;
        } catch (ResponseException e) {
            log.info(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_GET_HOTEL_INFO);
        }
    }
}
