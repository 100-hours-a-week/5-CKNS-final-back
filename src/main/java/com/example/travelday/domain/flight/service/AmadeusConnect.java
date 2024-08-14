package com.example.travelday.domain.flight.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmadeusConnect {

    private final Amadeus amadeus;

    public AmadeusConnect(
        @Value("${amadeus.api-key}") String apiKey,
        @Value("${amadeus.api-secret}") String apiSecret) {

        this.amadeus = Amadeus
                           .builder(apiKey, apiSecret)
                           .build();
    }

    public FlightOfferSearch[] flights(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.get(
            Params.with("originLocationCode", origin)
                .and("destinationLocationCode", destination)
                .and("departureDate", departDate)
                .and("returnDate", returnDate)
                .and("adults", adults)
                .and("max", 3));
    }
}