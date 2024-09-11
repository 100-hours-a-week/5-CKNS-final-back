package com.example.travelday.domain.settlement.repository;

import com.example.travelday.domain.settlement.entity.SettlementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementDetailRepository extends JpaRepository<SettlementDetail, Long> {
}
