package com.example.travelday.domain.supersale.dto.response;

import com.example.travelday.domain.supersale.entity.RawFlightOffer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record FlightPriceResDto(
    String airlineCode,
    String flightNumber,
    String origin,
    String destination,
    String departDate,
    String totalPrice,
    String currency
) {
    public static FlightPriceResDto of(RawFlightOffer rawFlightOffer, ObjectMapper objectMapper) {
        try {
            // JSON 파싱
            JsonNode flightData = objectMapper.readTree(rawFlightOffer.getRawFlightOffer());

            // 필요한 값 추출
            String airlineCode = flightData.get("itineraries").get(0).get("segments").get(0).get("carrierCode").asText();
            String flightNumber = flightData.get("itineraries").get(0).get("segments").get(0).get("number").asText();
            String origin = flightData.get("itineraries").get(0).get("segments").get(0).get("departure").get("iataCode").asText();
            String destination = flightData.get("itineraries").get(0).get("segments").get(0).get("arrival").get("iataCode").asText();
            String departDate = flightData.get("itineraries").get(0).get("segments").get(0).get("departure").get("at").asText();
            String totalPrice = flightData.get("price").get("grandTotal").asText();
            String currency = flightData.get("price").get("currency").asText();

            // FlightPriceResDto로 변환
            return FlightPriceResDto.builder()
                        .airlineCode(airlineCode)
                        .flightNumber(flightNumber)
                        .origin(origin)
                        .destination(destination)
                        .departDate(departDate)
                        .totalPrice(totalPrice)
                        .currency(currency)
                        .build();
        } catch (Exception e) {
            log.error("Failed to parse flight data", e.getMessage());
            return null;
        }
    }
}
