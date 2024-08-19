package com.example.travelday.domain.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightResDto {

    @Comment("항공편 아이디")
    private String id;

    @Comment("출발지 IATA 코드")
    private String originIataCode;

    @Comment("도착지 IATA 코드")
    private String destinationIataCode;

    @Comment("출발 일자")
    private String departureDate;

    @Comment("도착 일자")
    private String arrivalDate;

    @Comment("항공사 코드")
    private String carrierCode;

    @Comment("항공편 번호")
    private String flightNumber;

    @Comment("좌석 수")
    private int numberOfBookableSeats;

    @Comment("가격 정보")
    private Price price;

    @Comment("탑승자 정보")
    private List<TravelerPricing> travelerPricings;

    // Getters and Setters

    public static class Price {

        @Comment("통화")
        private String currency;

        @Comment("총 가격")
        private double total;

        @Comment("기본 요금")
        private double base;

        // Getters and Setters
    }

    public static class TravelerPricing {

        @Comment("탑승자 아이디")
        private String travelerId;

        @Comment("탑승자 유형")
        private String travelerType;

        @Comment("탑승자 요금 옵션")
        private String fareOption;

        @Comment("탑승자 가격 정보")
        private Price price;

        @Comment("탑승자 세부 정보")
        private List<FareDetailsBySegment> fareDetailsBySegment;

        // Getters and Setters
    }

    public static class FareDetailsBySegment {

        @Comment("세그먼트 아이디")
        private String segmentId;

        @Comment("객실 유형")
        private String cabin;

        @Comment("운임 기준")
        private String fareBasis;

        @Comment("클래스")
        private String flightClass;

        @Comment("포함된 수하물 개수")
        private int includedCheckedBags;

        // Getters and Setters
    }
}