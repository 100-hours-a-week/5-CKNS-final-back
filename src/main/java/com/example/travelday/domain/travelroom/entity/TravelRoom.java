package com.example.travelday.domain.travelroom.entity;


import com.example.travelday.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "travel_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "travel_room_name")
    private String name;

    public static TravelRoom create(String name) {
        TravelRoom travelRoom = new TravelRoom();
        travelRoom.name = name;
        return travelRoom;
    }
}
