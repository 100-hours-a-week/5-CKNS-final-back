package com.example.travelday.domain.settlement.dto.request;

public record SettlementReqDto(
        String title,
        String content,
        Long amount
) {
}
