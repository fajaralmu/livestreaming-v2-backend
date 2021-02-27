package com.fajar.livestreaming.service.conference;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.entity.ConferenceRoom;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.repository.ConferenceRoomRepository;
import com.fajar.livestreaming.service.SessionValidationService;

@Service
public class PublicConferenceService {

	@Autowired
	private ConferenceRoomRepository conferenceRoomRepository;
	@Autowired
	private SessionValidationService sessionValidationService;
	
	public WebResponse generateRoom(HttpServletRequest httpRequest) {
		User user = sessionValidationService.getLoggedUser(httpRequest);
		ConferenceRoom room = getExsitingRoom(user);
		if (room == null) {
			room = ConferenceRoom.createNew(user);
		}
		conferenceRoomRepository.save(room);
		return WebResponse.builder().conferenceRoom(room.toModel()).build();
	}
	private ConferenceRoom getExsitingRoom(User user) {
		ConferenceRoom room = conferenceRoomRepository.findTop1ByUser(user);
		return room;
	}
	public WebResponse getRoom(HttpServletRequest httpRequest) {
		User user = sessionValidationService.getLoggedUser(httpRequest);
		ConferenceRoom room = getExsitingRoom(user);
		return WebResponse.builder().conferenceRoom(room == null? null : room.toModel()).build();
	}

}
