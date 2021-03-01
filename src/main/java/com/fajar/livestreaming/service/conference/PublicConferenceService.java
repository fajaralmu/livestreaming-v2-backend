package com.fajar.livestreaming.service.conference;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.entity.ConferenceRoom;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.exception.ApplicationException;
import com.fajar.livestreaming.exception.DataNotFoundException;
import com.fajar.livestreaming.repository.ConferenceRoomRepository;
import com.fajar.livestreaming.repository.UserRepository;
import com.fajar.livestreaming.service.SessionValidationService;

@Service
public class PublicConferenceService {

	@Autowired
	private ConferenceRoomRepository conferenceRoomRepository;
	@Autowired
	private SessionValidationService sessionValidationService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserRepository UserRepository;
	@Autowired
	private PublicConferenceNotifier notifier;

	public WebResponse generateRoom(HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getExsitingRoomOwnedByUser(user);
		if (room == null) {
			room = ConferenceRoom.createNew(user);
		}
		conferenceRoomRepository.save(room);
		return WebResponse.builder().conferenceRoom(room.toModel()).build();
	}

	private ConferenceRoom getExsitingRoomOwnedByUser(User user) {
		ConferenceRoom room = conferenceRoomRepository.findTop1ByUser(user);
		return room;
	}

	public WebResponse getUserRoom(HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getExsitingRoomOwnedByUser(user);
		return WebResponse.builder().conferenceRoom(room == null ? null : room.toModel()).build();
	}
	public WebResponse getRoom(String code, HttpServletRequest httpRequest) { 
		ConferenceRoom room = conferenceRoomRepository.findTop1ByCode(code);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		if (room.isActive() ==false) {
			throw new ApplicationException("Room Not Active");
		}
		return WebResponse.builder().conferenceRoom(room.toModel()).build();
	}
	public WebResponse enterRoom(String code, HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = conferenceRoomRepository.findTop1ByCode(code);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		if (room.isActive() ==false) {
			throw new ApplicationException("Room Not Active");
		}
		if (room.addMember(user)) {
			conferenceRoomRepository.save(room);
			notifier.notifyNewMemberAdded(room, user);
		}
		return WebResponse.builder().conferenceRoom(room.toModel()).build();
	}

	private User getUser(HttpServletRequest httpRequest) {
		User user = sessionValidationService.getLoggedUser(httpRequest);
		return user;
	}

	public WebResponse updateActiveStatus(boolean active, HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getExsitingRoomOwnedByUser(user);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		room.setActive(active);
		conferenceRoomRepository.save(room);
		if (active == false) {
			notifier.notifyRoomInvalidated(room);
		}
		return WebResponse.success();
	}

	public WebResponse deleteUserRoom(HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getExsitingRoomOwnedByUser(user);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		Session session = sessionFactory.openSession();
		 Transaction tx = session.beginTransaction();
		try {
			session.delete(room);
			tx.commit();
			return WebResponse.success();
		} catch (Exception e) {
			if (tx != null) {
				tx.commit();
			}
			throw new ApplicationException(e);
		} finally {
			session.close();
		}
		 
	}

	public WebResponse removeRoomMember(String userCode, HttpServletRequest httpRequest) {
		ConferenceRoom room = getExsitingRoomOwnedByUser(httpRequest);
		User member = UserRepository.findTop1ByCode(userCode);
		room.removeMember(member);
		conferenceRoomRepository.save(room);
		notifier.notifyMemberRemoved(room, member);
		return WebResponse.success();
	}

	private ConferenceRoom getExsitingRoomOwnedByUser(HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		return getExsitingRoomOwnedByUser(user);
	}

	public WebResponse leaveRoom(String code, HttpServletRequest httpRequest) {
		User member = getUser(httpRequest);
		ConferenceRoom room = conferenceRoomRepository.findTop1ByCode(code);
		if (room.isAdmin(member)) {
			return updateActiveStatus(false, httpRequest);
		}
		
		room.removeMember(member);
		conferenceRoomRepository.save(room);
		notifier.notifyMemberRemoved(room, member);
		return WebResponse.success();
	}

}
