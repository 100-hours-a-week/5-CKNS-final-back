package com.example.travelday.domain.travelroom.repository;

import com.example.travelday.domain.travelroom.entity.TravelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRoomRepository extends JpaRepository<TravelRoom, Long> {

}