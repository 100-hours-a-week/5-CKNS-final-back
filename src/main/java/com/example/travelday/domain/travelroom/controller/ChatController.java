package com.example.travelday.domain.travelroom.controller;

import com.example.travelday.domain.travelroom.dto.request.ChatReqDto;
import com.example.travelday.domain.travelroom.dto.response.ChatResDto;
import com.example.travelday.domain.travelroom.service.ChatService;
import com.example.travelday.global.common.ApiResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/{travelRoomId}/chat/send")
    @SendTo("/topic/{travelRoomId}/chat")
    public ChatResDto sendChatMessage(@DestinationVariable("travelRoomId") Long travelRoomId, @Payload ChatReqDto chatReqDto, @AuthenticationPrincipal UserDetails userDetails) {
        return chatService.saveChat(travelRoomId, chatReqDto, userDetails.getUsername());
    }

    /**
     * 채팅 조회
     */
    @GetMapping("/rooms/{travelRoomId}/chat")
    public ResponseEntity<ApiResponseEntity<?>> listChats(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponseEntity.of(chatService.getAllChat(travelRoomId, userDetails.getUsername())));
    }

}
