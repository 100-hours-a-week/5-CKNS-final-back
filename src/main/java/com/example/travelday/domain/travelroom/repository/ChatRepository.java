package com.example.travelday.domain.travelroom.repository;

import com.example.travelday.domain.travelroom.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findAllByTravelRoomId(Long travelRoomId);
}
