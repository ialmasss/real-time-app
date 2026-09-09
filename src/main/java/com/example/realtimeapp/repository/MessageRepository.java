package com.example.realtimeapp.repository;

import com.example.realtimeapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{
    List<Message> findByChat_IdOrderBySentAtAsc(Long chatId);
}
