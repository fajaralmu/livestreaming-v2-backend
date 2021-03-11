package com.fajar.livestreaming.service.conference;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.WebRequest;
import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.dto.model.ConferenceRoomModel;
import com.fajar.livestreaming.entity.ChatMessage;
import com.fajar.livestreaming.entity.ConferenceRoom;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.exception.ApplicationException;
import com.fajar.livestreaming.exception.DataNotFoundException;
import com.fajar.livestreaming.repository.ChatMessageRepository;
import com.fajar.livestreaming.repository.ConferenceRoomRepository;
import com.fajar.livestreaming.repository.UserRepository;
import com.fajar.livestreaming.service.SessionValidationService;
import com.fajar.livestreaming.service.config.DefaultConfigurationService;

@Service
public class PublicConferenceService {

	@Autowired
	private ConferenceRoomRepository conferenceRoomRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private SessionValidationService sessionValidationService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserRepository UserRepository;
	@Autowired
	private PublicConferenceNotifier notifier;
	@Autowired
	private DefaultConfigurationService defaultConfigurationService;

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
		ConferenceRoom room = getRoomByCode(code);

		if (room.isActive() == false) {
			throw new ApplicationException("Room Not Active");
		}
		return WebResponse.builder().conferenceRoom(room.toModel()).build();
	}

	public WebResponse enterRoom(String code, HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getRoomByCode(code);
		if (room.isActive() == false) {
			throw new ApplicationException("Room Not Active");
		}
		if (room.addMember(user)) {
			conferenceRoomRepository.save(room);
			notifier.notifyNewMemberAdded(room, user);
		}
		List<ChatMessage> chats = chatMessageRepository.findByRoomOrderByIdAsc(room);
		room.setChats(chats);
		ConferenceRoomModel roomModel = room.toModel();
		roomModel.setConfig(defaultConfigurationService.getAndCheckApplicationConfiguration().toModel());
		return WebResponse.builder().conferenceRoom(roomModel).build();
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
			removeChat(room);
		}
		return WebResponse.success();
	}

	private ConferenceRoom getRoomByCode(String code) {
		ConferenceRoom room = conferenceRoomRepository.findTop1ByCode(code);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		return room;
	}

	public WebResponse deleteUserRoom(HttpServletRequest httpRequest) {
		User user = getUser(httpRequest);
		ConferenceRoom room = getExsitingRoomOwnedByUser(user);
		if (null == room) {
			throw new DataNotFoundException("Room not found");
		}
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<ChatMessage> chats = chatMessageRepository.findByRoomOrderByIdAsc(room);
		try {
			chats.forEach(c -> {
				session.delete(c);
			});
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

	private void removeChat(ConferenceRoom room) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<ChatMessage> chats = chatMessageRepository.findByRoomOrderByIdAsc(room);
		try {
			chats.forEach(c -> {
				session.delete(c);
			});

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
		ConferenceRoom room = getRoomByCode(code);
		if (room.isAdmin(member)) {
			return updateActiveStatus(false, httpRequest);
		}

		room.removeMember(member);
		conferenceRoomRepository.save(room);
		notifier.notifyMemberRemoved(room, member);
		return WebResponse.success();
	}

	public WebResponse notifyUserEnterRoom(String code, HttpServletRequest httpRequest) {
		User member = getUser(httpRequest);
		ConferenceRoom room = getRoomByCode(code);
		notifier.notifyMemberEnterRoom(room, member);
		return WebResponse.success();
	}

	public WebResponse sendChatMessage(WebRequest request, HttpServletRequest httpRequest) {
		User member = getUser(httpRequest);
		String roomCode = request.getChatMessage().getRoomCode();
		ConferenceRoom room = getRoomByCode(roomCode);

		ChatMessage message = request.getChatMessage().toEntity();
		message.setUser(member);
		room.addMessage(message);
		ConferenceRoom savedRoom = conferenceRoomRepository.save(room);
		notifier.notifyChatMessage(room, message);
		return WebResponse.builder().conferenceRoom(savedRoom.toModel()).build();
	}

	public WebResponse updateRoomInfo(WebRequest webRequest, HttpServletRequest httpRequest) {
		
		ConferenceRoom room = getExsitingRoomOwnedByUser(httpRequest);
		if (null == room) {
			throw new DataNotFoundException("ROOM NOT FOUND");
		}
		ConferenceRoomModel roomModel = webRequest.getConferenceRoom();
		
		return WebResponse.builder().conferenceRoom(updateAndSaveRoomInfo(room, roomModel)).build();
	}

	private ConferenceRoomModel updateAndSaveRoomInfo(ConferenceRoom room, ConferenceRoomModel roomModel) {
		 
		if (null != roomModel.getCode()) {
			room.setCode(roomModel.getCode());
		}
		 
		ConferenceRoom saved = conferenceRoomRepository.save(room);
		return saved.toModel();
	}

}
