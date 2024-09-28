package com.example.travelday.domain.chat.controller;

import com.example.travelday.domain.chat.dto.request.ChatReqDto;
import com.example.travelday.domain.chat.dto.response.ChatMessageResDto;
import com.example.travelday.domain.chat.dto.response.ChatResDto;
import com.example.travelday.domain.chat.service.ChatService;
import com.example.travelday.domain.chat.utils.ChatBucket;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.utils.BucketUtils;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    @Value("${bucket.chat-ban-milliseconds}")
    private long CHAT_BAN_MILLISECONDS;

    @Value("${bucket.consume-count}")
    private int CONSUME_BUCKET_COUNT;

    private final ChatService chatService;

    private final BucketUtils bucketUtils;

    private final ChatBucket chatBucket;

    private final SimpMessagingTemplate messagingTemplate;
    // TODO: 세션아이디와 사용자정보 redis에 저장?
    // 사용자와 세션 ID 매핑
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    private final Map<String, Long> userBanEndTime = new ConcurrentHashMap<>();  // 사용자별 제한 시간 기록
    /**
     * WebSocket 연결 시 세션 ID 가져오기
     */
    @EventListener(SessionConnectEvent.class)
    public void onConnect(SessionConnectEvent event) {
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();

        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) event.getMessage().getHeaders().get("nativeHeaders");

        String userId = Optional.ofNullable(nativeHeaders)
                            .flatMap(headers -> Optional.ofNullable(headers.get("UserId")))
                            .map(list -> list.get(0))
                            .orElseThrow(() -> new IllegalStateException("UserId not found in nativeHeaders"));

        sessions.put(sessionId, userId);
    }

    /**
     * WebSocket 연결 종료 시 세션 ID 삭제
     */
    @EventListener(SessionDisconnectEvent.class)
    public void onDisconnect(SessionDisconnectEvent event) {
        sessions.remove(event.getSessionId());
    }

    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/chat/rooms/{travelRoomId}")
    @SendTo("/sub/chat/rooms/{travelRoomId}")
    public ChatMessageResDto sendChatMessage(@DestinationVariable("travelRoomId") Long travelRoomId, @Payload String message, SimpMessageHeaderAccessor accessor) {
        String senderId = sessions.get(accessor.getSessionId());

        // todo: 검증 코드 분리
        // 세션 ID나 사용자 정보가 없을 때 예외 처리
        if (senderId == null) {
            log.error("Invalid session ID or user is not authenticated.");
            throw new IllegalStateException("User is not authenticated or session is invalid."); // todo: 에러코드 정의
        }

        // 메시지 길이 제한 (최대 5000자)
        if (message.length() > 5000) {
            log.error("Message exceeds the maximum allowed length of 5000 characters.");
            throw new IllegalArgumentException("Message exceeds the maximum allowed length of 5000 characters."); // todo: 에러코드 정의
        }

        if (userBanEndTime.containsKey(senderId) && System.currentTimeMillis() < userBanEndTime.get(senderId)) {
            return null;
        }

        Bucket bucket = chatBucket.resolveBucket(senderId);
        long leftBucketToken = bucket.getAvailableTokens();

        if (!bucket.tryConsume(CONSUME_BUCKET_COUNT)) {
            long banEndTime = System.currentTimeMillis() + CHAT_BAN_MILLISECONDS;
            userBanEndTime.put(senderId, banEndTime);
            return null;
        }

        ChatReqDto chatReqDto = ChatReqDto.builder()
                .senderId(senderId)
                .message(message)
                .build();

        return ChatMessageResDto.of(chatService.saveChat(travelRoomId, chatReqDto), leftBucketToken);
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
    @GetMapping("/rooms/last")
    public ResponseEntity<ApiResponseEntity<List<ChatResDto>>> getLastChats(@AuthenticationPrincipal UserDetails userDetails) {
        List<ChatResDto> chatResDtos = chatService.getLastChatsByTravelRoomId(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponseEntity.of(chatResDtos));
    }
}
