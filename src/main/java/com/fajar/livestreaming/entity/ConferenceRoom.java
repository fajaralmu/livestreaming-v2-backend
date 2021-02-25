package com.fajar.livestreaming.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fajar.livestreaming.dto.model.ConferenceRoomModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conference_room")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceRoom extends BaseEntity<ConferenceRoomModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3809687775944837620L;
	@Column(unique = true)
	private String code;
	@Column
	private boolean active;
	@JoinColumn(name = "user_id", updatable = false)
	private User user;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "room_member", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "room_id") })
	@Default
	private Set<User> members = new HashSet<>();
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "room_chats", joinColumns = { @JoinColumn(name = "message_id") }, inverseJoinColumns = {
			@JoinColumn(name = "room_id") })
	@Default
	private Set<ChatMessage> chats = new HashSet<>();

	public void addMembers(User member) {
		members.add(member);
	}

	public void removeMembers() {
		members.clear();
	}
	
	@Override
	public ConferenceRoomModel toModel()  {
		ConferenceRoomModel model = getModelInstance();
		copy(model, "members", "chats");
		if (members != null) {
			members.forEach(m-> model.addMember(m.toModel()));
		}
		if (chats != null) {
			chats.forEach(c-> model.addChat(c.toModel()));
		}
		return model;
	}

}
