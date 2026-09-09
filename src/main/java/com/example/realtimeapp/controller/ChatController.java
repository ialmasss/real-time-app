package com.example.realtimeapp.controller;

import com.example.realtimeapp.dto.ChatDto;
import com.example.realtimeapp.dto.CreateChatRequest;
import com.example.realtimeapp.dto.MessageDto;
import com.example.realtimeapp.dto.SendMessageRequest;
import com.example.realtimeapp.model.Chat;
import com.example.realtimeapp.model.Message;
import com.example.realtimeapp.model.User;
import com.example.realtimeapp.repository.ChatRepository;
import com.example.realtimeapp.repository.MessageRepository;
import com.example.realtimeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createChat(@RequestBody CreateChatRequest request, Authentication authentication) {

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Set<User> participants = new HashSet<>(userRepository.findAllById(request.getParticipantIds()));
        participants.add(currentUser);

        Chat chat = new Chat();
        chat.setName(request.getName());
        chat.setGroup(request.isGroup());
        chat.setParticipants(participants);

        Chat savedChat = chatRepository.save(chat);

        return ResponseEntity.ok(new ChatDto(savedChat));
    }

    @GetMapping
    public ResponseEntity<?> getMyChats(Authentication authentication) {

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<ChatDto> chats = chatRepository.findByParticipants_Id(currentUser.getId())
                .stream()
                .map(ChatDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(chats);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request, Authentication authentication) {

        User sender = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Chat chat = chatRepository.findById(request.getChatId())
                .orElseThrow(() -> new RuntimeException("Чат не найден"));

        Message message = new Message();
        message.setContent(request.getContent());
        message.setChat(chat);
        message.setSender(sender);

        Message savedMessage = messageRepository.save(message);

        return ResponseEntity.ok(new MessageDto(savedMessage));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long chatId) {

        List<MessageDto> messages = messageRepository.findByChat_IdOrderBySentAtAsc(chatId)
                .stream()
                .map(MessageDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(messages);
    }
}