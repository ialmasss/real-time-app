package com.example.realtimeapp.controller;

import com.example.realtimeapp.dto.ChatMessageRequest;
import com.example.realtimeapp.dto.MessageDto;
import com.example.realtimeapp.model.Message;
import com.example.realtimeapp.model.User;
import com.example.realtimeapp.model.Chat;
import com.example.realtimeapp.repository.ChatRepository;
import com.example.realtimeapp.repository.MessageRepository;
import com.example.realtimeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import com.example.realtimeapp.dto.TypingRequest;
import com.example.realtimeapp.dto.TypingNotification;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequest request, Principal principal){

        Chat chat = chatRepository.findById(request.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat is not found"));

        User sender = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException(("User is not found")));

        Message message = new Message();
        message.setContent(request.getContent());
        message.setChat(chat);
        message.setSender(sender);

        Message savedMessage = messageRepository.save(message);

        MessageDto messageDto = new MessageDto(savedMessage);

        messagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), messageDto);
    }

    @MessageMapping("/chat.typing")
    public void handleTyping(TypingRequest request, Principal principal){

        System.out.println("ПОЛУЧЕНО событие печати от: " + principal.getName() + ", isTyping=" + request.isTyping());

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User is not found"));

        TypingNotification notification = new TypingNotification(user.getUsername(), request.isTyping());

        messagingTemplate.convertAndSend("/topic/chat/" + request.getChatId() + "/typing", notification);
    }


}