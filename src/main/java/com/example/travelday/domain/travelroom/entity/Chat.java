package com.example.travelday.domain.travelroom.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat")
public class Chat {

    @Id
    private ObjectId id;

    @Field("travel_room_id")
    private Long travelRoomId;

    @Field("sender_id")
    private String senderId;

    private String message;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Chat(Long travelRoomId, String senderId, String message) {
        this.travelRoomId = travelRoomId;
        this.senderId = senderId;
        this.message = message;
    }
}
