package com.fajar.livestreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.livestreaming.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage	, Long> {

}
