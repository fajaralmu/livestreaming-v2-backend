package com.fajar.livestreaming.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.livestreaming.entity.ChatMessage;
import com.fajar.livestreaming.entity.ConferenceRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage	, Long> {

	List<ChatMessage> findByRoomOrderByIdAsc(ConferenceRoom room);
}
