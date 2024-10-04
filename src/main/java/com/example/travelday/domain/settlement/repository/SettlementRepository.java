package com.example.travelday.domain.settlement.repository;

import com.example.travelday.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    Optional<List<Settlement>> findByTravelRoomId(Long travelRoomId);

    Optional<Settlement> findByIdAndTravelRoomId(Long id, Long travelRoomId);

    List<Settlement> findAllByTravelRoomId(Long travelRoomId);
}
