package com.example.travelday.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.global.auth.oauth.enums.SocialType;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByUserIdAndSocialType(String userId, SocialType socialType);

    boolean existsByNickname(String nickname);
}
