package com.example.travelday.domain.travelroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/rooms/{travelRoomId}/plan")
@RequiredArgsConstructor
public class TravelPlanController {
}
