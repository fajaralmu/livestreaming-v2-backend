package com.fajar.livestreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.livestreaming.entity.ConferenceRoom;
import com.fajar.livestreaming.entity.User;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom	, Long> {

	ConferenceRoom findTop1ByUser(User user);

	ConferenceRoom findTop1ByCode(String code);

}
