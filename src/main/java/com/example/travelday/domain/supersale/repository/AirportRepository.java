package com.example.travelday.domain.supersale.repository;

import com.example.travelday.domain.supersale.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Integer> {

    List<Airport> findByKoreanAirportNameContaining(String koreanAirportName);

    List<Airport> findByAirportCodeContaining(String airportCode);
}
