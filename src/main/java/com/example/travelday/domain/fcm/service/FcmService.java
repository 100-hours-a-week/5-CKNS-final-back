package com.example.travelday.domain.fcm.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.fcm.entity.FcmToken;
import com.example.travelday.domain.fcm.repository.FcmTokenRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    private final MemberRepository memberRepository;

    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void createFcmToken(String userId, String fcmToken) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 중복 토큰 검사
        Optional<FcmToken> existingToken = fcmTokenRepository.findByToken(fcmToken);
        if (existingToken.isPresent()) {
            throw new CustomException(ErrorCode.FCM_TOKEN_DUPLICATE);
        }

        FcmToken token = FcmToken.builder()
                .token(fcmToken)
                .member(member)
                .build();

        fcmTokenRepository.save(token);
    }

    /*public void updateFcmToken(String oldFcmToken, String newFcmToken, Long memberId) {
        FcmToken token = fcmTokenRepository.findByToken(oldFcmToken)
                .orElseThrow(() -> new BusinessException(FcmException.NOT_FOUND_FCM));

        if (!token.getMember().getId().equals(memberId))
            throw new BusinessException(FcmException.NOT_MATCH_MEMBER);

        token.updateToken(newFcmToken);
        fcmTokenRepository.save(token);
    }*/

    @Transactional
    public void updateFcmToken(Long memberId, String fcmToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        FcmToken fcmTokenOptional= fcmTokenRepository.findByToken(fcmToken)
                .orElseThrow(() -> new CustomException(ErrorCode.FCM_TOKEN_NOT_FOUND));

        FcmToken token = FcmToken.builder()
                .token(fcmToken)
                .member(member)
                .build();

        fcmTokenRepository.save(token);
    }

    // FCM 토큰의 마지막 사용 시간을 현재 시간으로 업데이트
    @Transactional
    public void updateLastUsedTime(FcmToken fcmToken) {
        fcmToken.updateLastUsedTime();
        fcmTokenRepository.save(fcmToken);
    }

    @Transactional
    public void deleteFcmToken(String fcmToken) {
        FcmToken token = fcmTokenRepository.findByToken(fcmToken)
                .orElseThrow(() -> new CustomException(ErrorCode.FCM_TOKEN_NOT_FOUND));

        fcmTokenRepository.delete(token);
    }
}
