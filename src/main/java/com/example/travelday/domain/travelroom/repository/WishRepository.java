package com.example.travelday.domain.travelroom.repository;

import com.example.travelday.domain.travelroom.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findAllByTravelRoomId(Long travelRoomId);
}
