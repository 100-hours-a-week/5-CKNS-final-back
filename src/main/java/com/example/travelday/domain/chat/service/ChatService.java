package com.example.travelday.domain.chat.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.chat.dto.request.ChatReqDto;
import com.example.travelday.domain.chat.dto.response.ChatResDto;
import com.example.travelday.domain.chat.entity.Chat;
import com.example.travelday.domain.chat.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;

    private final MemberRepository memberRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, MemberRepository memberRepository) {
        this.chatRepository = chatRepository;
        this.memberRepository = memberRepository;
    }

    public ChatResDto saveChat(Long travelRoomId, ChatReqDto chatReqDto, String userId) {
        Chat chat = Chat.builder()
                        .travelRoomId(travelRoomId)
                        .senderId(userId)
                        .message(chatReqDto.message())
                        .build();

        Chat savedChat = chatRepository.save(chat);

        return ChatResDto.of(savedChat);
    }

    public String savedChat(Long travelRoomId, ChatReqDto chatReqDto, String userId) {
        Chat chat = Chat.builder()
                        .travelRoomId(travelRoomId)
                        .senderId(userId)
                        .message(chatReqDto.message())
                        .build();

        chatRepository.save(chat);

        return chat.getMessage();
    }

    public List<ChatResDto> getAllChat(Long travelRoomId, String userId) {
        return chatRepository.findAllByTravelRoomId(travelRoomId)
                    .stream()
                    .map( chat -> {
                        Optional<Member> chatUser = memberRepository.findByUserId(userId);
                        return ChatResDto.of(chat);
                    })
                    .toList();
    }

    public Chat getLastChatsByTravelRoomId(Long travelRoomId) {
        return chatRepository.findTopByTravelRoomIdOrderByCreatedAtDesc(travelRoomId);
    }
}
