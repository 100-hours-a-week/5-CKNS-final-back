package com.example.travelday.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat")
@Builder
public class Chat {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field("travel_room_id")
    private Long travelRoomId;

    @Field("sender_id")
    private String senderId;

    @Field("sender_nickname")
    private String senderNickname;

    @Field("message")
    private String message;

    @Field("createdAt")
    @CreatedDate
    private LocalDateTime createdAt;
}
