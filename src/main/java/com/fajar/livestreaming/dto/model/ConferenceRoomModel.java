package com.fajar.livestreaming.dto.model;

import java.util.HashSet;
import java.util.Set;

import com.fajar.livestreaming.annotation.Dto;
import com.fajar.livestreaming.annotation.FormField;
import com.fajar.livestreaming.constants.FieldType;
import com.fajar.livestreaming.entity.ConferenceRoom;
import com.fajar.livestreaming.exception.ApplicationException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Dto(editable = false)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceRoomModel extends BaseModel<ConferenceRoom> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -384967775944837620L;

	@FormField
	private String code;
	@FormField(type = FieldType.FIELD_TYPE_CHECKBOX)
	private boolean active;

	@FormField(optionItemName = "displayName")
	private UserModel user;

	@Default
	private Set<UserModel> members = new HashSet<>();
	@Default
	private Set<ChatMessageModel> chats = new HashSet<>();
	public void addMember(UserModel member) {
		members.add(member);
	}
	@Override
	public ConferenceRoom toEntity() {
		throw new ApplicationException("Not Allowed");
	}
	public void addChat(ChatMessageModel model) {
		chats.add(model);
	}

}
