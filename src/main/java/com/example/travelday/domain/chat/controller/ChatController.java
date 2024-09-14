package com.example.travelday.domain.chat.controller;

import com.example.travelday.domain.chat.dto.request.ChatReqDto;
import com.example.travelday.domain.chat.dto.response.ChatResDto;
import com.example.travelday.domain.chat.entity.Chat;
import com.example.travelday.domain.chat.service.ChatService;
import com.example.travelday.global.common.ApiResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // TODO: 세션아이디와 사용자정보 redis에 저장?
    // 사용자와 세션 ID 매핑
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * WebSocket 연결 시 세션 ID 가져오기
     */
    @EventListener(SessionConnectEvent.class)
    public void onConnect(SessionConnectEvent event) {
        log.info("Connect: {}", event);
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        String userId = event.getMessage().getHeaders().get("nativeHeaders").toString().split("userId=")[1].split("]")[0];

        sessions.put(sessionId, userId);
        log.info("웹소켓 연결 sessions: {}, userId: {} ", sessions, userId);
    }

    /**
     * WebSocket 연결 종료 시 세션 ID 삭제
     */
    @EventListener(SessionDisconnectEvent.class)
    public void onDisconnect(SessionDisconnectEvent event) {
        log.info("Disconnect: {}", event);
        sessions.remove(event.getSessionId());
        log.info("웹소켓 연결 종료 sessions: {}", sessions);
    }

    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/chat/rooms/{travelRoomId}")
    @SendTo("/sub/chat/rooms/{travelRoomId}")
    public ChatResDto sendChatMessage(@DestinationVariable("travelRoomId") Long travelRoomId, @Payload String message, SimpMessageHeaderAccessor accessor) {
        String senderId = sessions.get(accessor.getSessionId());

        // 세션 ID나 사용자 정보가 없을 때 예외 처리
        if (senderId == null) {
            log.error("Invalid session ID or user is not authenticated.");
            throw new IllegalStateException("User is not authenticated or session is invalid.");
        }

        ChatReqDto chatReqDto = ChatReqDto.builder()
                .senderId(senderId)
                .message(message)
                .build();

        return chatService.saveChat(travelRoomId, chatReqDto);
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
    // TODO: userDetail로 travelRoomId에 해당하는지 확인하는 로직 추가
    public ResponseEntity<ApiResponseEntity<ChatResDto>> getLastChats(@PathVariable Long travelRoomId) {
        Chat lastchat = chatService.getLastChatsByTravelRoomId(travelRoomId);
        ChatResDto chatResDto = ChatResDto.of(lastchat);
        return ResponseEntity.ok(ApiResponseEntity.of(chatResDto));
    }
}
