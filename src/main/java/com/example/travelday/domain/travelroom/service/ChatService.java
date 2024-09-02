package com.example.travelday.domain.travelroom.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.travelroom.dto.request.ChatReqDto;
import com.example.travelday.domain.travelroom.dto.response.ChatResDto;
import com.example.travelday.domain.travelroom.entity.Chat;
import com.example.travelday.domain.travelroom.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

        return ChatResDto.of(savedChat, userId);
    }

    public List<ChatResDto> getAllChat(Long travelRoomId, String userId) {
        return chatRepository.findAllByTravelRoomId(travelRoomId)
                    .stream()
                    .map( chat -> {
                        Optional<Member> chatUser = memberRepository.findByUserId(userId);
                        return ChatResDto.of(chat, chatUser.get().getUserId());
                    })
                    .toList();
    }
}
