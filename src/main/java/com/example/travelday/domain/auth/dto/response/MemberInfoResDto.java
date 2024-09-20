package com.example.travelday.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoResDto(String nickname, String profileImagePath) {}
