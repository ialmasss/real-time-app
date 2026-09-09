package com.example.realtimeapp.dto;

import com.example.realtimeapp.model.Message;
import java.time.LocalDateTime;

public class MessageDto {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private UserDto sender;
    private Long chatId;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
        this.sender = new UserDto(message.getSender());
        this.chatId = message.getChat().getId();
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public UserDto getSender() { return sender; }
    public Long getChatId() { return chatId; }
}