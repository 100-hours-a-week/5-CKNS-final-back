package com.example.travelday.domain.travelroom.repository;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.entity.UserTravelRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTravelRoomRepository extends JpaRepository<UserTravelRoom, Long> {
    Optional<List<UserTravelRoom>> findByMember(Member member);

    Optional<UserTravelRoom> findByMemberAndTravelRoomId(Member member, Long travelRoomId);

    List<UserTravelRoom> findByTravelRoom(TravelRoom travelRoom);

    boolean existsByTravelRoomId(Long travelRoomId);
}