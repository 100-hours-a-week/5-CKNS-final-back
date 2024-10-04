package com.example.travelday.domain.supersale.repository;

import com.example.travelday.domain.supersale.entity.RawFlightOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawFlightOfferRepository extends JpaRepository<RawFlightOffer, Long> {
}
