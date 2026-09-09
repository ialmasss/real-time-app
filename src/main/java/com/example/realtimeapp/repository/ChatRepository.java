package com.example.realtimeapp.repository;

import com.example.realtimeapp.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByParticipants_Id(Long UserId);
}