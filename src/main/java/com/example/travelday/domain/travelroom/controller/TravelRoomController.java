package com.example.travelday.domain.travelroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class TravelRoomController {

    @GetMapping
    public String getRooms() {
        return "rooms";
    }

    @GetMapping("/{id}")
    public String getRoom(@PathVariable Long id) {
        return "room";
    }

    @PostMapping
    public String createRoom() {
        return "create";
    }

    @PutMapping("/{id}")
    public String updateRoom(@PathVariable Long id) {
        return "update";
    }

    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable Long id) {
        return "delete";
    }

}
