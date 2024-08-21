package com.example.travelday.domain.travelroom.entity;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "invitation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelroom_id", nullable = false)
    private TravelRoom travelRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    private Member inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    private Member invitee;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @Column(name = "update_At")
    private LocalDateTime updatedAt;

    @Builder
    public Invitation(TravelRoom travelRoom, Member inviter, Member invitee, InvitationStatus status) {
        this.travelRoom = travelRoom;
        this.inviter = inviter;
        this.invitee = invitee;
        this.status = status;
    }

    public void reject() {
        this.status = InvitationStatus.REJECTED;
    }

    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
    }
}
