package com.example.travelday.domain.settlement.dto.response;

import com.example.travelday.domain.settlement.entity.Settlement;
import com.example.travelday.domain.settlement.entity.enums.SettlementStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record SettlementResDto(
        Long id,
        SettlementStatus status,
        BigDecimal totalAmount,
        Date settledAt
) {
    public static SettlementResDto fromEntity(Settlement settlement) {
        return SettlementResDto.builder()
                .id(settlement.getId())
                .status(settlement.getStatus())
                .totalAmount(settlement.getTotalAmount())
                .settledAt(settlement.getSettledAt())
                .build();
    }
}
