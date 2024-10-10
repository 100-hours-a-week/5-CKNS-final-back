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

    List<UserTravelRoom> findAllByTravelRoomId(Long travelRoomI);

    List<UserTravelRoom> findByTravelRoom(TravelRoom travelRoom);

    Optional<List<UserTravelRoom>> findByTravelRoomId(Long travelRoomId);

    boolean existsByTravelRoomIdAndMember(Long travelRoomId, Member receiver);

    boolean existsByTravelRoomId(Long travelRoomId);

    int countByTravelRoomId(Long travelRoomId);
}