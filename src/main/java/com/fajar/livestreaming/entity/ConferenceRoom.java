package com.fajar.livestreaming.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fajar.livestreaming.dto.model.ConferenceRoomModel;
import com.fajar.livestreaming.util.StringUtil;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "conference_room")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ConferenceRoom extends BaseEntity<ConferenceRoomModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3809687775944837620L;
	@Column(unique = true)
	private String code;
	@Column
	@Setter(value = AccessLevel.NONE)
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name = "user_id" )
	private User user;

	@ManyToMany  (fetch = FetchType.EAGER)
	@JoinTable(name = "room_member", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "room_id") })
	@Default 
	private List<User> members = new ArrayList<>();

	@Transient
//	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//	@JoinTable(name = "room_chats", joinColumns = { @JoinColumn(name = "message_id") }, inverseJoinColumns = {
//			@JoinColumn(name = "room_id") })
	@Default 
	private List<ChatMessage> chats = new ArrayList<>();

	public boolean addMember(User member) {
		if (isMemberExist(member)) return false;
		members.add(member);
		return true;
	}

	public boolean isMemberExist(User member) {
		for (User user : members) {
			if (user.idEquals(member)) return true;
		}
		return false;
	}

	public void setActive(boolean active) {
		this.active = active;
		if (!active) {
			removeMembers();
			removeMessages();
		}
	}
	
	private void removeMessages() {
		chats.clear();
	}

	/**
	 * remove members except room OWNER
	 */
	public void removeMembers() {
		members.clear();
		members.add(getUser());
	}

	@Override
	public ConferenceRoomModel toModel() {
		ConferenceRoomModel model = getModelInstance();
		copy(model, "members", "chats");
		if (members != null) {
			members.forEach(m -> model.addMember(m.toModel()));
		}
		if (chats != null) {
			chats.forEach(c -> model.addChat(c.toModel()));
		}
		return model;
	}

	public static ConferenceRoom createNew(User user2) {
		ConferenceRoom room = new ConferenceRoom();
		room.setUser(user2);
		room.setCode(randomRoomCode());
		room.addMember(user2);
		return room;
	}

	private static String randomRoomCode() {
		return StringUtil.generateRandomNumber(5);
	}

	public void removeMember(User memberToRemove) {
		if (isAdmin(memberToRemove)) {
			log.info("CANNOT REMOVE ROOM ADMIN");
			return;
		}
		for (User member : members) {
			if (member.idEquals(memberToRemove)) {
				members.remove(member);
				break;
			}
		}
	}

	public boolean isAdmin(User member) {
		try {
			return user.idEquals(member);
		}catch (Exception e) {
			log.error("Error {}", e);
			return false;
		}
	}

	public void addMessage(ChatMessage entity) {
		chats.add(entity);
	}

}
