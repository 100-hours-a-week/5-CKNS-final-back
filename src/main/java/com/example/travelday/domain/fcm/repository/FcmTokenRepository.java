package com.example.travelday.domain.fcm.repository;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.fcm.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByToken(String token);

    List<FcmToken> findByMember(Member member);
}
