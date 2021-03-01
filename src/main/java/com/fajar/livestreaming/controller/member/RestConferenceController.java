package com.fajar.livestreaming.controller.member;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.livestreaming.annotation.CustomRequestInfo;
import com.fajar.livestreaming.controller.BaseController;
import com.fajar.livestreaming.dto.WebRequest;
import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.service.LogProxyFactory;
import com.fajar.livestreaming.service.conference.PublicConferenceService;
import com.fajar.livestreaming.service.config.DefaultUserService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/member/conference")
@Slf4j
public class RestConferenceController extends BaseController {

	@Autowired
	private PublicConferenceService publicConferenceService;
	 
	public RestConferenceController() {
		log.info("------------------RestConferenceController-----------------");
	}

	@PostConstruct
	public void init() {
		LogProxyFactory.setLoggers(this);
	}

	
	@PostMapping(value = "/getuserroom", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getUserRoom(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.getUserRoom(httpRequest);
	}
	@PostMapping(value = "/deleteuserroom", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse deleteUserRoom(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.deleteUserRoom(httpRequest);
	}
	@PostMapping(value = "/generateroom", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse generateroom(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.generateRoom(httpRequest);
	}
	
	 
	@PostMapping(value = "/leaveroom/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse leaveRoom(@PathVariable(name="code")String code,HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.leaveRoom(code, httpRequest);
	}
	@PostMapping(value = "/getroom/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse getRoom(@PathVariable(name="code")String code,HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.getRoom(code, httpRequest);
	}
	@PostMapping(value = "/enterroom/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse enterRoom(@PathVariable(name="code")String code,HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.enterRoom(code, httpRequest);
	}
	@PostMapping(value = "/notifyuserenterroom/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse notifyUserEnterRoom(@PathVariable(name="code")String code,HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.notifyUserEnterRoom(code, httpRequest);
	}
	@PostMapping(value = "/removeroommember/{userCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse removeRoomMember(@PathVariable(name="userCode")String userCode,HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.removeRoomMember(userCode, httpRequest);
	}
	
	@PostMapping(value = "/updateactivestatus/{active}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WebResponse updateactivestatus(@PathVariable(name="active")String active,HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		return publicConferenceService.updateActiveStatus(Boolean.valueOf(active), httpRequest);
	}
}
