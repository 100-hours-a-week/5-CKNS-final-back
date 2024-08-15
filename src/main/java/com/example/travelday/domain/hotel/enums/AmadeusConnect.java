package com.example.travelday.domain.hotel.enums;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.HotelOfferSearch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AmadeusConnect {

    private final Amadeus amadeus;

    private AmadeusConnect(@Value("${amadeus.api.key}") String apiKey,
                           @Value("${amadeus.api.secret}") String apiSecret) {
        this.amadeus = Amadeus
                .builder(apiKey, apiSecret)
                .build();
    }

    public Hotel[] location(String cityCode) throws ResponseException {
        return amadeus.referenceData.locations.hotels.byCity.get(Params
                .with("cityCode", cityCode));
    }

    public Hotel[] hotelsByGeocode(double latitude, double longitude) throws ResponseException {
        return amadeus.referenceData.locations.hotels.byGeocode.get(Params
                .with("latitude", latitude)
                .and("longitude", longitude));
    }

    public HotelOfferSearch[] hotelOffers(List<String> hotelIds) throws ResponseException {
        return amadeus.shopping.hotelOffersSearch.get(Params
                .with("hotelIds", hotelIds));
    }
}
