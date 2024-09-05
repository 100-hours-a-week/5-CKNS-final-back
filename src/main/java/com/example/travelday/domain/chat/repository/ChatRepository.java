package com.example.travelday.domain.chat.repository;

import com.example.travelday.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findAllByTravelRoomId(Long travelRoomId);
}
