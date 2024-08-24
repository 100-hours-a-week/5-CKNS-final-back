package com.example.travelday.domain.supersale.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "raw_flight_offers")
@Getter
@NoArgsConstructor
public class FlightOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_offer_data", columnDefinition = "JSON")
    private String flightOfferData;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt = new java.util.Date();

    public FlightOffer (String flightOfferData) {
        this.flightOfferData = flightOfferData;
    }
}