package com.fajar.livestreaming.dto.model;

import java.util.Date;

import com.fajar.livestreaming.annotation.Dto;
import com.fajar.livestreaming.annotation.FormField;
import com.fajar.livestreaming.constants.FieldType;
import com.fajar.livestreaming.constants.MessageDestination;
import com.fajar.livestreaming.entity.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Dto(editable = false, creatable = false)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageModel extends BaseModel<ChatMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -90687775944837620L;
	 
	@FormField
	private MessageDestination destinationType; 
	@FormField
	private String destination;
	@FormField
	private String body;
	@FormField(type = FieldType.FIELD_TYPE_DATETIME)
	private Date date;
	@FormField(optionItemName = "displayName")
	private UserModel user;
	@FormField(optionItemName = "code")
	private ConferenceRoomModel room;
	private String roomCode;

	 

}
