package com.example.travelday.domain.hotel.dto.response;

import com.amadeus.resources.HotelOfferSearch;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.util.Arrays;

@Data
@Builder
public class HotelResDto {

    @Comment("호텔 아이디")
    private String hotelId;

    @Comment("위도")
    private double latitude;

    @Comment("경도")
    private double longitude;

    @Comment("도시 코드")
    private String cityCode;

    @Comment("이용 가능 여부")
    private boolean available;

    @Comment("호텔 정보 제공")
    private Offer offer;

    @Data
    @Builder
    public static class Offer {

        @Comment("체크인 날짜")
        private String checkInDate;

        @Comment("체크아웃 날짜")
        private String checkOutDate;

        @Comment("방 정보")
        private Room room;

        @Comment("가격 정보")
        private Price price;
    }

    @Data
    @Builder
    public static class Room {

        @Comment("방 타입")
        private String type;

        @Comment("방 세부 타입")
        private TypeEstimated typeEstimated;
    }

    @Data
    @Builder
    public static class TypeEstimated {

        @Comment("침대 수")
        private double beds;

        @Comment("침대 유형")
        private String bedType;
    }

    @Data
    @Builder
    public static class Price {

        @Comment("통화")
        private String currency;

        @Comment("호텔 총 가격")
        private String total;
    }

    public static HotelResDto of(HotelOfferSearch hotel) {
        return HotelResDto.builder()
                .hotelId(hotel.getHotel().getHotelId())
                .latitude(hotel.getHotel().getLatitude())
                .longitude(hotel.getHotel().getLongitude())
                .cityCode(hotel.getHotel().getCityCode())
                .available(hotel.isAvailable())
                .offer(Arrays.stream(hotel.getOffers())
                        .findFirst()
                        .map(offer -> Offer.builder()
                            .checkInDate(offer.getCheckInDate())
                            .checkOutDate(offer.getCheckOutDate())
                            .room(Room.builder()
                                    .type(offer.getRoom().getType())
                                    .typeEstimated(TypeEstimated.builder()
                                            .beds(offer.getRoom().getTypeEstimated().getBeds())
                                            .bedType(offer.getRoom().getTypeEstimated().getBedType())
                                            .build())
                                    .build())
                            .price(Price.builder()
                                    .currency(offer.getPrice().getCurrency())
                                    .total(offer.getPrice().getTotal())
                                    .build())
                            .build())
                        .orElse(null))
                .build();
    }
}
