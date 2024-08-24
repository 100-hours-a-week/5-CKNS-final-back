package com.example.travelday.domain.supersale.dto.response;

import com.amadeus.resources.FlightOfferSearch;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FlightResDto {

    @Comment("마지막 티켓팅 날짜")
    private String lastTicketingDate;

    @Comment("예약 가능한 좌석 수")
    private int numberOfBookableSeats;

    @Comment("여정 정보")
    private List<ItineraryDto> itineraries;

    @Comment("가격 정보")
    private PriceDto price;

    @Comment("여행자 가격 정보")
    private List<TravelerPricingDto> travelerPricings;

    @Data
    @Builder
    public static class ItineraryDto {

        @Comment("소요 시간")
        private String duration;

        @Comment("세그먼트 정보")
        private List<SegmentDto> segments;

        @Data
        @Builder
        public static class SegmentDto {

            @Comment("출발지 정보")
            private LocationDto departure;

            @Comment("도착지 정보")
            private LocationDto arrival;

            @Comment("항공사 코드")
            private String carrierCode;

            @Comment("세그먼트 소요 시간")
            private String duration;

            @Comment("경유 횟수")
            private int numberOfStops;

            @Data
            @Builder
            public static class LocationDto {

                @Comment("IATA 코드")
                private String iataCode;

                @Comment("출발/도착 시간")
                private String at;
            }
        }
    }

    @Data
    @Builder
    public static class PriceDto {

        @Comment("통화")
        private String currency;

        @Comment("총 가격")
        private String total;

        @Comment("최종 총 가격")
        private String grandTotal;
    }

    @Data
    @Builder
    public static class TravelerPricingDto {

        @Comment("여행자 ID")
        private String travelerId;

        @Comment("운임 옵션")
        private String fareOption;

        @Comment("여행자 유형")
        private String travelerType;

        @Comment("세그먼트별 운임 세부 정보")
        private List<FareDetailsBySegmentDto> fareDetailsBySegment;

        @Data
        @Builder
        public static class FareDetailsBySegmentDto {

            @Comment("세그먼트 ID")
            private String segmentId;

            @Comment("좌석 클래스")
            private String cabin;

            @Comment("포함된 수하물 정보")
            private IncludedCheckedBagsDto includedCheckedBags;

            @Data
            @Builder
            public static class IncludedCheckedBagsDto {

                @Comment("포함된 수하물 개수")
                private int quantity;
            }
        }
    }

    public static FlightResDto of(FlightOfferSearch flight) {
        // Map itineraries
        List<FlightResDto.ItineraryDto> itineraries = Arrays.stream(flight.getItineraries())
                .map(itinerary -> FlightResDto.ItineraryDto.builder()
                        .duration(itinerary.getDuration())
                        .segments(Arrays.stream(itinerary.getSegments())
                                .map(segment -> FlightResDto.ItineraryDto.SegmentDto.builder()
                                        .departure(FlightResDto.ItineraryDto.SegmentDto.LocationDto.builder()
                                                .iataCode(segment.getDeparture().getIataCode())
                                                .at(segment.getDeparture().getAt())
                                                .build())
                                        .arrival(FlightResDto.ItineraryDto.SegmentDto.LocationDto.builder()
                                                .iataCode(segment.getArrival().getIataCode())
                                                .at(segment.getArrival().getAt())
                                                .build())
                                        .carrierCode(segment.getCarrierCode())
                                        .duration(segment.getDuration())
                                        .numberOfStops(segment.getNumberOfStops())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        // Map price
        FlightResDto.PriceDto price = FlightResDto.PriceDto.builder()
                .currency(flight.getPrice().getCurrency())
                .total(flight.getPrice().getTotal())
                .grandTotal(flight.getPrice().getGrandTotal())
                .build();

        // Map traveler pricings
        List<FlightResDto.TravelerPricingDto> travelerPricings = Arrays.stream(flight.getTravelerPricings())
                .map(travelerPricing -> FlightResDto.TravelerPricingDto.builder()
                        .travelerId(travelerPricing.getTravelerId())
                        .fareOption(travelerPricing.getFareOption())
                        .travelerType(travelerPricing.getTravelerType())
                        .fareDetailsBySegment(Arrays.stream(travelerPricing.getFareDetailsBySegment())
                                .map(fareDetail -> FlightResDto.TravelerPricingDto.FareDetailsBySegmentDto.builder()
                                        .segmentId(fareDetail.getSegmentId())
                                        .cabin(fareDetail.getCabin())
                                        .includedCheckedBags(FlightResDto.TravelerPricingDto.FareDetailsBySegmentDto.IncludedCheckedBagsDto.builder()
                                                .quantity(fareDetail.getIncludedCheckedBags().getQuantity())
                                                .build())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return FlightResDto.builder()
                .lastTicketingDate(flight.getLastTicketingDate())
                .numberOfBookableSeats(flight.getNumberOfBookableSeats())
                .itineraries(itineraries)
                .price(price)
                .travelerPricings(travelerPricings)
                .build();
    }
}
