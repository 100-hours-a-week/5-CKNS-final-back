package com.example.travelday.domain.supersale.dto.response;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.Itinerary;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.util.Arrays;

@Data
@Builder
public class FlightResDto {

    @Comment("출발지 정보")
    private Departure departure;

    @Comment("도착지 정보")
    private Arrival arrival;

    @Comment("항공 가격 정보")
    private Price price;

    @Comment("편도 여부")
    private boolean oneWay;

    @Comment("예약 가능한 좌석 수")
    private int numberOfBookableSeats;

    @Data
    @Builder
    public static class Departure {

        @Comment("항공 코드")
        private String iataCode;

        @Comment("국가 코드")
        private String countryCode;

        @Comment("출발 시간")
        private String at;
    }

    @Data
    @Builder
    public static class Arrival {

        @Comment("항공 코드")
        private String iataCode;

        @Comment("국가 코드")
        private String countryCode;

        @Comment("도착 시간")
        private String at;
    }

    @Data
    @Builder
    public static class Price {

        @Comment("통화")
        private String currency;

        @Comment("항공 총 가격")
        private String total;
    }

    public static FlightResDto of(FlightOfferSearch flight) {
        Itinerary itinerary = Arrays.stream(flight.getItineraries()).findFirst().get();
        FlightOfferSearch.SearchSegment segment = Arrays.stream(itinerary.getSegments()).findFirst().get();

        Departure departure = Departure.builder()
                .iataCode(segment.getDeparture().getIataCode())
                .countryCode(null) // 국가 코드는 추가적으로 dictionaries에서 찾아서 설정
                .at(segment.getDeparture().getAt())
                .build();

        Arrival arrival = Arrival.builder()
                .iataCode(segment.getArrival().getIataCode())
                .countryCode(null) // 국가 코드는 추가적으로 dictionaries에서 찾아서 설정
                .at(segment.getArrival().getAt())
                .build();

        Price price = Price.builder()
                .currency(flight.getPrice().getCurrency())
                .total(flight.getPrice().getTotal())
                .build();

        return FlightResDto.builder()
                .departure(departure)
                .arrival(arrival)
                .price(price)
                .oneWay(flight.isOneWay())
                .numberOfBookableSeats(flight.getNumberOfBookableSeats())
                .build();
    }
}
