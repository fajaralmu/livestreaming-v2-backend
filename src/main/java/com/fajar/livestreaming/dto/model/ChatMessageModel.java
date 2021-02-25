package com.fajar.livestreaming.dto.model;

import java.util.Date;

import com.fajar.livestreaming.constants.MessageDestination;
import com.fajar.livestreaming.entity.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageModel extends BaseModel<ChatMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -90687775944837620L;
	 
	private MessageDestination destinationType; 
	private String destination; 
	private String body; 
	private Date date; 
	private UserModel user;

	 

}
