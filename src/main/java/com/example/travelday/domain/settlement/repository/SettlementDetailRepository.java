package com.example.travelday.domain.settlement.repository;

import com.example.travelday.domain.settlement.entity.SettlementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SettlementDetailRepository extends JpaRepository<SettlementDetail, Long> {
    List<SettlementDetail> findBySettlementId(Long settlementId);
}
