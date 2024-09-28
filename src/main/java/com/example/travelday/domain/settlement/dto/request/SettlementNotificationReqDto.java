package com.example.travelday.domain.settlement.dto.request;

import java.math.BigDecimal;
import java.util.Map;

public record SettlementNotificationReqDto(Map<String, BigDecimal> nicknameToPrice) {
}