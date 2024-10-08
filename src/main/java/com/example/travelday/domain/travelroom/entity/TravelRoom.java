package com.example.travelday.domain.travelroom.entity;


import com.example.travelday.domain.travelroom.dto.request.TravelRoomReqDto;
import com.example.travelday.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Size(max = 15, message = "최대 15자리까지 입력 가능합니다.")
    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "user_count", nullable = false)
    private int userCount = 1;

    @Builder
    public TravelRoom(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static TravelRoom addOf(TravelRoomReqDto requestDto) {
        return TravelRoom.builder()
                    .name(requestDto.name())
                    .startDate(requestDto.startDate())
                    .endDate(requestDto.endDate())
                    .build();
    }

    public void update(TravelRoomReqDto requestDto) {
        this.name = requestDto.name();
        this.startDate = requestDto.startDate();
        this.endDate = requestDto.endDate();
    }
}
