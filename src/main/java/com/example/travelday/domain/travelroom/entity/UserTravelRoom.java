package com.example.travelday.domain.travelroom.entity;

import com.example.travelday.domain.auth.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_travel_room", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "travel_room_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTravelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "travel_room_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TravelRoom travelRoom;

    @Column(name = "travel_room_name")
    private String travelRoomName;

    public static UserTravelRoom create(TravelRoom savedTravelRoom, Member member) {
        UserTravelRoom userTravelRoom = new UserTravelRoom();
        userTravelRoom.member = member;
        userTravelRoom.travelRoom = savedTravelRoom;
        return userTravelRoom;
    }
}
