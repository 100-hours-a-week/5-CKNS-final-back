package com.example.travelday.domain.invitation.repository;

import com.example.travelday.domain.invitation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
