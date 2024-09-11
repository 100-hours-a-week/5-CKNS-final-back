package com.example.travelday.domain.settlement.entity;

import com.example.travelday.domain.settlement.entity.enums.SettlementStatus;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "settlement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SettlementStatus status = SettlementStatus.PENDING;

    @Column(name = "settled_date")
    @Temporal(TemporalType.DATE)
    private Date settledDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_room_id", nullable = false)
    private TravelRoom travelRoom;

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettlementDetail> settlementDetails;
}