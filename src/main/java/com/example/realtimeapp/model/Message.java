package com.example.realtimeapp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JoinColumnOrFormula;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToMany
    @JoinTable(
            name = "message_read_by",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> readBy = new HashSet<>();

    public Message(){
        this.sentAt =  LocalDateTime.now();
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getContent(){ return content; }
    public void setContent(String content){ this.content = content; }

    public LocalDateTime getSentAt(){ return sentAt; }
    public void setSentAt(LocalDateTime sentAt){ this.sentAt = sentAt; }

    public Chat getChat(){ return chat; }
    public void setChat(Chat chat){ this.chat = chat; }

    public User getSender(){ return sender; }
    public void setSender(User sender){ this.sender = sender; }

    public Set<User> getReadBy() { return readBy;}
    public void setReadBy(Set<User> readBy) { this.readBy = readBy; }
}