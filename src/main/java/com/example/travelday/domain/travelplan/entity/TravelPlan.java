package com.example.travelday.domain.travelplan.entity;

import com.example.travelday.domain.travelplan.dto.request.TravelPlanReqDto;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "travel_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlan {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @JoinColumn(name = "travel_room_id", nullable = false)
        @ManyToOne(fetch = FetchType.LAZY)
        private TravelRoom travelRoom;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "scheduled_day", nullable = false)
        private int scheduledDay;

        @Column(name = "position", nullable = false)
        private int position;

        @Column(name = "latitude", nullable = false)
        private double latitude;

        @Column(name = "longitude", nullable = false)
        private double longitude;

        @Builder
        public TravelPlan(TravelRoom travelRoom, String name, int scheduledDay, int position, double latitude, double longitude) {
                this.travelRoom = travelRoom;
                this.name = name;
                this.scheduledDay = scheduledDay;
                this.position = position;
                this.latitude = latitude;
                this.longitude = longitude;
        }

        public static TravelPlan addOf(TravelPlanReqDto travelPlanReqDto, TravelRoom travelRoom, int position) {
                return TravelPlan.builder()
                                .travelRoom(travelRoom)
                                .name(travelPlanReqDto.name())
                                .scheduledDay(travelPlanReqDto.scheduledDay())
                                .position(position)
                                .latitude(travelPlanReqDto.latitude())
                                .longitude(travelPlanReqDto.longitude())
                                .build();
        }
}
