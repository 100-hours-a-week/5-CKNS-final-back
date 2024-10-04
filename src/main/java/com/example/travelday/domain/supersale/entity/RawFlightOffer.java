package com.example.travelday.domain.supersale.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "raw_flight_offers")
public class RawFlightOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_offer_data", columnDefinition = "JSON")
    private String rawFlightOffer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
