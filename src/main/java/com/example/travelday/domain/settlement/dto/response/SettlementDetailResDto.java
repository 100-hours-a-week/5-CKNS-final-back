package com.example.travelday.domain.settlement.dto.response;

import com.example.travelday.domain.settlement.entity.SettlementDetail;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SettlementDetailResDto(
    Long id,
    Long settlementId,
    String name,
    BigDecimal amount
) {
    public static SettlementDetailResDto fromEntity(SettlementDetail settlementDetail) {
        return SettlementDetailResDto.builder()
                    .id(settlementDetail.getId())
                    .settlementId(settlementDetail.getSettlement().getId())
                    .name(settlementDetail.getName())
                    .amount(settlementDetail.getAmount())
                    .build();
    }
}
