package com.example.travelday.domain.invitation.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
public record InvitationReqDto(
        String invitee
) {
}
