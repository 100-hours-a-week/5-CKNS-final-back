package com.example.travelday.domain.travelroom.repository;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelRoomRepository extends JpaRepository<TravelRoom, Long> {

    // 특정 사용자와 연관된 모든 TravelRoom 조회
    List<TravelRoom> findAllByMember(Member member);

    // 특정 사용자와 연관된 특정 TravelRoom 조회
    Optional<TravelRoom> findByIdAndMember(Long id, Member member);
}