package com.example.travelday.domain.settlement.utils;

import com.example.travelday.domain.settlement.entity.Settlement;
import com.example.travelday.domain.settlement.entity.SettlementDetail;
import com.example.travelday.domain.settlement.repository.SettlementDetailRepository;
import com.example.travelday.domain.settlement.repository.SettlementRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SettlementDetailChangedListener {

    private final SettlementRepository settlementRepository;

    private final SettlementDetailRepository settlementDetailRepository;

    @EventListener
    @Transactional
    public void handleSettlementDetailChanged(SettlementDetailChangedEvent event) {
        Long settlementId = event.getSettlementId();

        Settlement settlement = settlementRepository.findById(settlementId)
                            .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND));

        BigDecimal totalAmount = settlementDetailRepository.findBySettlementId(settlementId)
                            .stream()
                            .map(SettlementDetail::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

        settlement.updateTotalAmount(totalAmount);
        settlementRepository.save(settlement);
    }
}
