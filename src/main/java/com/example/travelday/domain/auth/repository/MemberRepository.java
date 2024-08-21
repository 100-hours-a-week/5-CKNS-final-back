package com.example.travelday.domain.auth.repository;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.global.auth.oauth.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserIdAndSocialType(String userId, SocialType socialType);

    Member findByUserId(String userId);

}
