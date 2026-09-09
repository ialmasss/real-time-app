package com.example.realtimeapp.dto;

import java.util.List;

public class CreateChatRequest {
    private String name;
    private boolean isGroup;
    private List<Long> participantIds;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isGroup() { return isGroup; }
    public void setGroup(boolean group) { isGroup = group; }

    public List<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<Long> participantIds) { this.participantIds = participantIds; }
}