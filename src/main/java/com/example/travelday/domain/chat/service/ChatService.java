package com.example.travelday.domain.chat.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.chat.dto.request.ChatReqDto;
import com.example.travelday.domain.chat.dto.response.ChatResDto;
import com.example.travelday.domain.chat.entity.Chat;
import com.example.travelday.domain.chat.repository.ChatRepository;
import com.example.travelday.domain.travelroom.entity.UserTravelRoom;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final MemberRepository memberRepository;
    private final UserTravelRoomRepository userTravelRoomRepository;

    public ChatResDto saveChat(Long travelRoomId, ChatReqDto chatReqDto) {

        Member member = memberRepository.findByUserId(chatReqDto.senderId())
                            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Chat chat = Chat.builder()
                        .travelRoomId(travelRoomId)
                        .senderId(chatReqDto.senderId())
                        .senderNickname(member.getNickname())
                        .message(chatReqDto.message())
                        .build();

        Chat savedChat = chatRepository.save(chat);

        return ChatResDto.of(savedChat);
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

    public List<ChatResDto> getLastChatsByTravelRoomId(String userId) {

        Member member = memberRepository.findByUserId(userId)
                                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        log.info("채팅 서비스 member 찾기 : " + member);
        List<UserTravelRoom> userTravelRooms = userTravelRoomRepository.findByMember(member)
                                        .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));
        log.info("채팅 서비스 userTravelRooms 찾기 : " + userTravelRooms);



        // 각 travelRoomId에서 마지막 채팅을 Optional로 처리하고, 없는 경우는 건너뛰도록 처리
        List<Chat> lastchats = userTravelRooms.stream()
                                .map(userTravelRoom -> chatRepository.findTopByTravelRoomIdOrderByCreatedAtDesc(userTravelRoom.getTravelRoom().getId()))
                                .filter(Optional::isPresent) // 채팅이 존재하는 경우만 필터링
                                .map(Optional::get)           // Optional에서 Chat 객체 추출
                                .collect(Collectors.toList()); // 최종 리스트로 수집

        return lastchats.stream()
                        .map(ChatResDto::of)
                        .collect(Collectors.toList());
    }
}
