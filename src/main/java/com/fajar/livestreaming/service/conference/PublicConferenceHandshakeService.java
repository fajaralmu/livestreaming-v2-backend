package com.fajar.livestreaming.service.conference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.CommonRealtimeHandshake;
import com.fajar.livestreaming.dto.WebRequest;
import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.service.RealtimeService2;

@Service
public class PublicConferenceHandshakeService {

	@Autowired
	private RealtimeService2 realtimeService;
	
	public void handshakeWebRtc(CommonRealtimeHandshake request) {
		String roomId = request.getRoomCode ();
		String originId = request.getOrigin ();
		String eventId = request.getEventId();
		String destination = request.getDestination();

		CommonRealtimeHandshake response = CommonRealtimeHandshake.builder().origin(originId).eventId(eventId)
				.webRtcObject(request.getWebRtcObject()).build();
		realtimeService.convertAndSend("/wsResp/webrtcpublicconference/" + roomId + "/" + destination, 
				WebResponse.builder().realtimeHandshake(response).build());
		 
	}
	public WebResponse leaveRoom(WebRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public WebResponse sendMessage(WebRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public WebResponse togglePeerStream(CommonRealtimeHandshake request) {
//		String originId = request.getOrigin();
//		String roomId = request.getRoomCode();
//		boolean streamEnabled = request.isStreamEnabled();
//		WebResponse response = WebResponse.builder().streamEnabled(streamEnabled).requestId(originId).build();
//		
//		conferenceDataRepository.updateEnableStream(roomId, originId, streamEnabled);
//		
//		realtimeService.convertAndSend("/wsResp/togglepeerstream/" + roomId, response);
//		return response ;
		return null;
	}

	public void peerconfirm(CommonRealtimeHandshake request) {
		String roomId = request.getRoomCode();
		String peerId = request.getDestination();
		CommonRealtimeHandshake response = CommonRealtimeHandshake.builder().origin(request.getOrigin()).build();
		
		realtimeService.convertAndSend("/wsResp/peerconfirm/" + roomId+"/"+peerId, 
				WebResponse.builder().realtimeHandshake(response).build());
//		return response;
	}

}
