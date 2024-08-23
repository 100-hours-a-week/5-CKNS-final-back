package com.example.travelday.domain.travelroom.entity;


import com.example.travelday.domain.travelroom.dto.request.TravelRoomCreateReqDto;
import com.example.travelday.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Builder
    public TravelRoom(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static TravelRoom addOf(TravelRoomCreateReqDto requestDto) {
        return TravelRoom.builder()
                    .name(requestDto.name())
                    .startDate(requestDto.startDate())
                    .endDate(requestDto.endDate())
                    .build();
    }
}
