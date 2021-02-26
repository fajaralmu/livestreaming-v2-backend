package com.fajar.livestreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.livestreaming.entity.ConferenceRoom;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom	, Long> {

}
