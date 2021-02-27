package com.fajar.livestreaming.service.conference;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.entity.ConferenceRoom;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.exception.ApplicationException;
import com.fajar.livestreaming.exception.DataNotFoundException;
import com.fajar.livestreaming.repository.ConferenceRoomRepository;
import com.fajar.livestreaming.service.SessionValidationService;

@Service
public class PublicConferenceService {

	@Autowired
	private ConferenceRoomRepository conferenceRoomRepository;
	@Autowired
	private SessionValidationService sessionValidationService;

	public WebResponse generateRoom(HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
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

	public WebResponse getUserRoom(HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getExsitingRoom(user);
		return WebResponse.builder().conferenceRoom(room == null ? null : room.toModel()).build();
	}
	public WebResponse getRoom(String code, HttpServletRequest httpRequest) {
		return joinRoom(code, httpRequest);
	}

	public WebResponse joinRoom(String code, HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = conferenceRoomRepository.findTop1ByCode(code);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		room.addMember(user);
		conferenceRoomRepository.save(room);
		return WebResponse.builder().conferenceRoom(room.toModel()).build();
	}

	private User getUser(HttpServletRequest httpRequest) {
		User user = sessionValidationService.getLoggedUser(httpRequest);
		return user;
	}

	public WebResponse updateActiveStatus(boolean active, HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getExsitingRoom(user);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		room.setActive(active);
		conferenceRoomRepository.save(room);
		return WebResponse.success();
	}

}
