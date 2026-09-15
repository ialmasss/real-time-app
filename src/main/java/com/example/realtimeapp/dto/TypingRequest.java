package com.example.realtimeapp.dto;

public class TypingRequest {
    private Long chatId;
    private boolean isTyping;

    public Long getChatId(){ return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public boolean isTyping() { return isTyping; }
    public void setTyping(boolean typing) { isTyping = typing; }
}