package com.example.realtimeapp.dto;

import com.example.realtimeapp.model.User;

public class UserDto {
    private Long id;
    private String email;
    private String username;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
}