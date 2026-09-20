package com.example.realtimeapp.dto;

import com.example.realtimeapp.model.Message;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.realtimeapp.model.User;

public class MessageDto {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private UserDto sender;
    private Long chatId;
    private List<Long> readByUserIds;

    public MessageDto() {
    }

    public MessageDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
        this.sender = new UserDto(message.getSender());
        this.chatId = message.getChat().getId();
        this.readByUserIds = message.getReadBy().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public UserDto getSender() { return sender; }
    public void setSender(UserDto sender) { this.sender = sender; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public List<Long> getReadByUserIds() { return readByUserIds; }
    public void setReadByUserIds(List<Long> readByUserIds) { this.readByUserIds = readByUserIds; }
}