package com.example.travelday.domain.supersale.repository;

import com.example.travelday.domain.supersale.entity.FlightOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FlightOfferRepository extends JpaRepository<FlightOffer, Long> {

    @Transactional
    default void saveFlightOffer(List<FlightOffer> flightOffers) {
        this.saveAll(flightOffers);
    }
}
