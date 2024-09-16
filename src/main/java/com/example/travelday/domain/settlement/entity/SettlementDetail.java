package com.example.travelday.domain.settlement.entity;

import com.example.travelday.domain.settlement.dto.request.SettlementDetailReqDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "settlement_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public SettlementDetail(Settlement settlement, String name, BigDecimal amount) {
        this.settlement = settlement;
        this.name = name;
        this.amount = amount;
    }

    public static SettlementDetail addOf(Settlement settlement, SettlementDetailReqDto settlementDetailReqDto) {
        return SettlementDetail.builder()
                    .name(settlementDetailReqDto.name())
                    .settlement(settlement)
                    .amount(BigDecimal.valueOf(settlementDetailReqDto.amount()))
                    .build();
    }

    public void updateDetails(SettlementDetailReqDto settlementDetailReqDto) {
        this.name = settlementDetailReqDto.name();
        this.amount = BigDecimal.valueOf(settlementDetailReqDto.amount());
    }
}