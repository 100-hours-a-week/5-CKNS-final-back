package com.example.travelday.domain.chat.controller;

import com.example.travelday.domain.chat.dto.request.ChatReqDto;
import com.example.travelday.domain.chat.dto.response.ChatResDto;
import com.example.travelday.domain.chat.entity.Chat;
import com.example.travelday.domain.chat.service.ChatService;
import com.example.travelday.global.common.ApiResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/{travelRoomId}/chat/send")
    @SendTo("/{travelRoomId}/chat")
    public ChatResDto sendChatMessage(@DestinationVariable("travelRoomId") Long travelRoomId, @Payload ChatReqDto chatReqDto, @AuthenticationPrincipal UserDetails userDetails) {
        return chatService.saveChat(travelRoomId, chatReqDto, userDetails.getUsername());
    }

    /**
     * 채팅 메시지 전송( HTTP TEST )
     */
    @PostMapping("/{travelRoomId}/send")
    public ResponseEntity<ApiResponseEntity<ChatResDto>> sendChat(@PathVariable Long travelRoomId,  @RequestBody ChatReqDto chatReqDto, @AuthenticationPrincipal UserDetails userDetails) {
        ChatResDto saveChat = chatService.saveChat(travelRoomId, chatReqDto, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(saveChat));
    }

    /**
     * 채팅 조회
     */
    @GetMapping("/rooms/{travelRoomId}")
    public ResponseEntity<ApiResponseEntity<?>> listChats(@PathVariable Long travelRoomId, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponseEntity.of(chatService.getAllChat(travelRoomId, userDetails.getUsername())));
    }

    /**
     * 마지막 채팅 조회
     */
    @GetMapping("/rooms/{travelRoomId}/last")
    public ResponseEntity<ApiResponseEntity<ChatResDto>> getLastChats(@PathVariable Long travelRoomId) {
        Chat lastchat = chatService.getLastChatsByTravelRoomId(travelRoomId);
        log.info("lastchat : {}", lastchat);
        ChatResDto chatResDto = ChatResDto.of(lastchat);
        log.info("lastchatResDto : {}", chatResDto);
        return ResponseEntity.ok(ApiResponseEntity.of(chatResDto));
    }
}
