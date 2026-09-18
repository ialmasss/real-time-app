package com.example.realtimeapp.service;

import com.example.realtimeapp.dto.MessageDto;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            MessageDto messageDto = objectMapper.readValue(json, MessageDto.class);

            messagingTemplate.convertAndSend("/topic/chat/" + messageDto.getChatId(), messageDto);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обработать сообщение из Redis", e);
        }
    }
}