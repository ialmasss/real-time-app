package com.example.realtimeapp.dto;

public class TypingNotification {
    private String username;
    private boolean isTyping;

    public TypingNotification(String username, boolean isTyping){
        this.username = username;
        this.isTyping = isTyping;
    }

    public String getUsername() { return username; }
    public boolean isTyping() { return isTyping; }
}