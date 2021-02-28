package com.fajar.livestreaming.controller.member;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.livestreaming.controller.BaseController;
import com.fajar.livestreaming.dto.WebRequest;
import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.service.conference.PublicConferenceHandshakeService;
import com.fajar.livestreaming.service.conference.PublicConferenceService;

@CrossOrigin
@RestController
public class WebSocketWebRTCConference1Controller extends BaseController {
	Logger log = LoggerFactory.getLogger(WebSocketWebRTCConference1Controller.class);

	@Autowired
	private PublicConferenceHandshakeService publicConference1Service; 

	public WebSocketWebRTCConference1Controller() {
		log.info("------------------WebSocketpublicconferenceController #1-----------------");
	}

	@PostConstruct
	public void init() {// LogProxyFactory.setLoggers(this);

	}

	/////////////////////////////////////// WEbsocket
	/////////////////////////////////////// //////////////////////////////////////////
	
	@MessageMapping("/publicconference/webrtc")
	public void webrtc(WebRequest request) throws IOException {
		publicConference1Service.handshakeWebRtc(request.getRealtimeHandshake());
	}

//	@MessageMapping("/publicconference/join")
//	public WebResponse join(WebRequest request) throws IOException { 
//		return publicConference1Service.joinRoom(request);
//	}

	@MessageMapping("/publicconference/leave")
	public WebResponse leave(WebRequest request) throws IOException {
		return publicConference1Service.leaveRoom(request);
	}
	
	@MessageMapping("/publicconference/newchat")
	public WebResponse newchat(WebRequest request) throws IOException {
		return publicConference1Service.sendMessage(request);
	}
	
	@MessageMapping("/publicconference/togglepeerstream")
	public WebResponse togglepeerstream(WebRequest request) throws IOException {
		return publicConference1Service.togglePeerStream(request.getRealtimeHandshake());
	}
	
	@MessageMapping("/peerconfirm")
	public void peerconfirm(WebRequest request) throws IOException {
		publicConference1Service.peerconfirm(request.getRealtimeHandshake());
	}

}
