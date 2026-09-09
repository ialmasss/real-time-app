package com.example.realtimeapp.dto;

public class SendMessageRequest {
    private Long chatId;
    private String content;

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}