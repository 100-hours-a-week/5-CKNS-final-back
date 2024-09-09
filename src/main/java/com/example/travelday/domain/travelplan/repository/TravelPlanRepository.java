package com.example.travelday.domain.travelplan.repository;

import com.example.travelday.domain.travelplan.entity.TravelPlan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {
    List<TravelPlan> findAllByTravelRoomId(Long travelRoomId);

    @Query("SELECT COALESCE(MAX(tp.position), 0) FROM TravelPlan tp WHERE tp.travelRoom.id = :travelRoomId AND tp.scheduledDay = :scheduledDay")
    Optional<Integer> findMaxOrderByTravelRoomIdAndScheduledDay(@Param("travelRoomId") Long travelRoomId, @Param("scheduledDay") int scheduledDay);

    @Modifying
    @Transactional
    @Query("UPDATE TravelPlan tp SET tp.scheduledDay = :scheduledDay, tp.position = :position WHERE tp.id = :id")
    void updateTravelPlan(@Param("id") Long id, @Param("scheduledDay") Integer scheduledDay, @Param("position") int position);
}

