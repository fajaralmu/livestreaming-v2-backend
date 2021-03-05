package com.fajar.livestreaming.entity;
  
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fajar.livestreaming.constants.MessageDestination;
import com.fajar.livestreaming.dto.model.ChatMessageModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_message")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseEntity<ChatMessageModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3840687775944837620L;
	@Column
	@Enumerated(EnumType.STRING)
	private MessageDestination destinationType;
	@Column
	private String destination;
	@Column
	private String body;
	@Column
	private Date date;
	
	@ManyToOne 
	@JoinColumn(name = "user_id", unique = true )
	private User user;
	@ManyToOne 
	@JoinColumn(name = "room_id" )
	private ConferenceRoom room;

	 

}
