package com.example.travelday.domain.settlement.utils;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SettlementDetailChangedEvent extends ApplicationEvent {

    private final Long settlementId;

    public SettlementDetailChangedEvent(Object source, Long settlementId) {
        super(source);
        this.settlementId = settlementId;
    }
}
