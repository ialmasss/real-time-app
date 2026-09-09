package com.example.realtimeapp.dto;

import com.example.realtimeapp.model.Chat;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatDto {
    private Long id;
    private String name;
    private boolean isGroup;
    private LocalDateTime createdAt;
    private Set<UserDto> participants;

    public ChatDto(Chat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.isGroup = chat.isGroup();
        this.createdAt = chat.getCreatedAt();
        this.participants = chat.getParticipants().stream()
                .map(UserDto::new)
                .collect(Collectors.toSet());
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isGroup() { return isGroup; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Set<UserDto> getParticipants() { return participants; }
}