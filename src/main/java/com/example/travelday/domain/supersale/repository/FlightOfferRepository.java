package com.example.travelday.domain.supersale.repository;

import com.example.travelday.domain.supersale.entity.FlightOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightOfferRepository extends JpaRepository<FlightOffer, Long> {
}
