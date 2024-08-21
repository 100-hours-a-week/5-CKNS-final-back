package com.example.travelday.domain.travelroom.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

        @Column(name = "day", nullable = false)
        private int day;

        @Column(name = "order", nullable = false)
        private int order;

        @Column(name = "latitude", nullable = false)
        private double latitude;

        @Column(name = "longitude", nullable = false)
        private double longitude;
}
