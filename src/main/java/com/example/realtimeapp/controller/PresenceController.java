package com.example.realtimeapp.controller;

import com.example.realtimeapp.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class PresenceController {

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/presence.get")
    public void getOnlineUsers(Principal principal) {
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/presence-snapshot",
                presenceService.getOnlineUsers()
        );
    }
}