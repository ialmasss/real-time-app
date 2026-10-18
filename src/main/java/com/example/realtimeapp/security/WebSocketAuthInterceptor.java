package com.example.realtimeapp.security;

import com.example.realtimeapp.model.User;
import com.example.realtimeapp.repository.ChatRepository;
import com.example.realtimeapp.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private static final String CHAT_TOPIC_PREFIX = "/topic/chat/";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.isTokenValid(token)) {
                    String email = jwtUtil.extractEmail(token);
                    accessor.setUser(() -> email);
                }
            }
        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {

            String destination = accessor.getDestination();

            if (destination == null || !destination.startsWith("/app/")) {
                throw new MessagingException("Access denied: clients can send only to /app/**");
            }
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith(CHAT_TOPIC_PREFIX)) {

                Long chatId = extractChatId(destination);
                Principal principal = accessor.getUser();

                if (chatId == null || principal == null) {
                    throw new MessagingException("Access denied");
                }

                User user = userRepository.findByEmail(principal.getName()).orElse(null);

                if (user == null || !chatRepository.existsByIdAndParticipants_Id(chatId, user.getId())) {
                    throw new MessagingException("Access denied: you are not a member of this chat");
                }
            }
        }

        return message;
    }

    private Long extractChatId(String destination) {
        String rest = destination.substring(CHAT_TOPIC_PREFIX.length());
        String idPart = rest.split("/")[0];
        try {
            return Long.parseLong(idPart);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}