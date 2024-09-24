package com.example.travelday.domain.auth.dto.response;

import com.example.travelday.domain.auth.entity.Member;
import lombok.Builder;

@Builder

public record MemberInfoResDto(
        String userId,
        String nickname,
        String profileImagePath
) {
    public static MemberInfoResDto of(Member member) {
        return MemberInfoResDto.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .profileImagePath(member.getProfileImagePath())
                .build();
    }
}
