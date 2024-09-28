package com.example.travelday.domain.notification.controller;

import com.example.travelday.domain.notification.dto.response.NotificationResDto;
import com.example.travelday.domain.notification.service.NotificationService;
import com.example.travelday.global.common.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<ApiResponseEntity<List<NotificationResDto>>> getNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        List<NotificationResDto> notificationResDtoList = notificationService.getNotificationsListByMember(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(notificationResDtoList));
    }
}
