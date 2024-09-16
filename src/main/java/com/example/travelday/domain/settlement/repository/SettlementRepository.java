package com.example.travelday.domain.settlement.repository;

import com.example.travelday.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    Optional<Settlement> findByTravelRoomId(Long travelRoomId);

    Optional<Settlement> findByIdAndTravelRoomId(Long id, Long travelRoomId);
}
