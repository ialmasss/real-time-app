package com.example.realtimeapp.dto;

public class ChatMessageRequest {
    private Long chatId;
    private Long senderId;
    private String content;

    public Long getChatId(){ return chatId;}
    public void setChatId(Long chatId) {this.chatId = chatId;}

    public String getContent(){ return content;}
    public void setContent(String content){this.content = content;}

}