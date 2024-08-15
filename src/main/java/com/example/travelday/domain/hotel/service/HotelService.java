package com.example.travelday.domain.hotel.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.HotelOfferSearch;
import com.example.travelday.domain.hotel.dto.response.HotelResDto;
import com.example.travelday.domain.hotel.enums.AmadeusConnect;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class HotelService {

    private final AmadeusConnect amadeusConnect;

    public List<HotelResDto> getHotelsByGeocode(double latitude, double longitude) {
        try {
            List<Hotel> result = List.of(amadeusConnect.hotelsByGeocode(latitude, longitude));

            List<String> hotelIds = result.stream()
                    .map(Hotel::getHotelId)
                    .toList();

            List<HotelOfferSearch> hotelOffers = List.of(amadeusConnect.hotelOffers(hotelIds));
            List<HotelResDto> hotelResDto = new ArrayList<>();

            for (HotelOfferSearch offer : hotelOffers) {
                hotelResDto.add(HotelResDto.of(offer));
            }

            return hotelResDto;
        } catch (ResponseException e) {
            log.info(e.getMessage());
            throw new CustomException(ErrorCode.FAIL_TO_GET_HOTEL_INFO);
        }
    }
}
