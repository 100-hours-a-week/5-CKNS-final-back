package com.example.travelday.domain.wish.entity;

import com.example.travelday.domain.wish.dto.request.WishReqDto;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "wish")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "travelroom_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TravelRoom travelRoom;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Builder
    public Wish(TravelRoom travelRoom, String name, double latitude, double longitude) {
        this.travelRoom = travelRoom;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Wish addOf(WishReqDto wishReqDto, TravelRoom travelRoom) {
        return Wish.builder()
                .travelRoom(travelRoom)
                .name(wishReqDto.name())
                .latitude(wishReqDto.latitude())
                .longitude(wishReqDto.longitude())
                .build();
    }
}
