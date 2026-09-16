package com.example.realtimeapp.dto;

public class ReadReceiptNotification {
    private Long messageId;
    private Long userId;

    public ReadReceiptNotification(Long messageId, Long userId) {
        this.messageId = messageId;
        this.userId = userId;
    }

    public Long getMessageId() { return messageId;}
    public Long getUserId() { return userId; }
}