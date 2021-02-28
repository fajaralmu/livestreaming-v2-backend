package com.fajar.livestreaming.dto;

import java.io.Serializable;
import java.util.List;

import com.fajar.livestreaming.dto.model.ApplicationProfileModel;
import com.fajar.livestreaming.dto.model.ChatMessageModel;
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
	private ApplicationProfileModel profile; 

	/**
	 * ==========end CRUD============
	 */
	private ChatMessageModel chatMessage;

	private Filter filter; 
	
	private BaseEntity entityObject;
	private AttachmentInfo attachmentInfo; 
	private List<BaseEntity> orderedEntities; 
	
	private boolean regularTransaction;
	
	private String imageData;  
	private String token;
	
	private CommonRealtimeHandshake realtimeHandshake;

}
