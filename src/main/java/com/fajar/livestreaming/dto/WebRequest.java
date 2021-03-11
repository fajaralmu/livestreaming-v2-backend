package com.fajar.livestreaming.dto;

import java.io.Serializable;
import java.util.List;

import com.fajar.livestreaming.dto.model.ApplicationConfigurationModel;
import com.fajar.livestreaming.dto.model.ApplicationProfileModel;
import com.fajar.livestreaming.dto.model.ChatMessageModel;
import com.fajar.livestreaming.dto.model.ConferenceRoomModel;
import com.fajar.livestreaming.dto.model.UserModel;
import com.fajar.livestreaming.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 110411933791444017L;


	
	/**
	 * ENTITY CRUD use lowerCase!!!
	 */

	private String entity;
	private UserModel user; 
	private ApplicationConfigurationModel applicationconfiguration;

	/**
	 * ==========end CRUD============
	 */
	private ChatMessageModel chatMessage;
	private ApplicationProfileModel profile; 
	private ConferenceRoomModel conferenceRoom;
	private Filter filter; 
	
	private BaseEntity entityObject;
	private AttachmentInfo attachmentInfo; 
	private List<BaseEntity> orderedEntities; 
	
	private boolean regularTransaction;
	
	private String imageData;  
	private String token;
	
	private CommonRealtimeHandshake realtimeHandshake;

}
