package com.example.travelday.domain.invitation.controller;

import com.example.travelday.domain.invitation.dto.request.InvitationReqDto;
import com.example.travelday.domain.invitation.dto.request.InvitationStatusReqDto;
import com.example.travelday.domain.invitation.service.InvitationService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms/{travelRoomId}/invitation")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping()
    public ResponseEntity<ApiResponseEntity<String>> createInvitation(@PathVariable Long travelRoomId,
                                                                      @RequestBody InvitationReqDto invitationReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        invitationService.createInvitation(travelRoomId, invitationReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_INVITATION));
    }

    @PutMapping("/{invitationId}")
    public ResponseEntity<ApiResponseEntity<String>> responseInvitation(@PathVariable Long travelRoomId,
                                                                        @PathVariable Long invitationId,
                                                                        @AuthenticationPrincipal UserDetails userDetails,
                                                                        @RequestBody InvitationStatusReqDto invitationResponseReqDto) {
        invitationService.responseInvitation(userDetails.getUsername(), travelRoomId, invitationId, invitationResponseReqDto.status());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_INVITATION_RESPONSE));
    }
}
