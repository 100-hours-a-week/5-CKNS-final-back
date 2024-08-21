package com.example.travelday.domain.travelroom.repository;

import com.example.travelday.domain.travelroom.entity.UserTravelRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelRoomRepository extends JpaRepository<UserTravelRoom, Long> {
}