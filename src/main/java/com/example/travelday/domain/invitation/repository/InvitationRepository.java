package com.example.travelday.domain.invitation.repository;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.invitation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByInviteeAndTravelRoomId(Member member, Long travelRoomId);
}
