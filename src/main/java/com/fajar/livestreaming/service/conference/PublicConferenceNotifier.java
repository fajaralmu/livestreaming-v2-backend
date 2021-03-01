package com.fajar.livestreaming.service.conference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.ConferenceUpdate;
import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.entity.ConferenceRoom;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.service.RealtimeService2;

@Service
public class PublicConferenceNotifier {

	@Autowired
	private RealtimeService2 realtimeService;
	
	public void notifyNewMemberAdded(ConferenceRoom room, User user) {
		realtimeService.convertAndSend(path(room), WebResponse.builder().conferenceUpdate(ConferenceUpdate.PEER_NEW).user(user.toModel()).build());
		
	}
	private String path(ConferenceRoom room) {
		 
		return "/wsResp/conference/"+room.getCode();
	}
	public void notifyMemberRemoved(ConferenceRoom room, User user) {
		realtimeService.convertAndSend(path(room), WebResponse.builder().conferenceUpdate(ConferenceUpdate.PEER_LEAVE).user(user.toModel()).build());
		
	}
	 
	public void notifyRoomInvalidated(ConferenceRoom room) {
		realtimeService.convertAndSend(path(room), WebResponse.builder().conferenceUpdate(ConferenceUpdate.ROOM_INVALIDATED).build());
		
	}

}
