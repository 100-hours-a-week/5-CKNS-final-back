package com.example.travelday.domain.supersale.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "airport")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "airport_code", nullable = false)
    private String airportCode;

    @Column(name = "english_airport_name", nullable = false)
    private String englishAirportName;

    @Column(name = "korean_airport_name", nullable = false)
    private String koreanAirportName;
}
