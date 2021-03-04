package com.fajar.livestreaming.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class CommonRealtimeHandshake implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -772335730474878936L;

	private String origin;
	private String destination;
	private String eventId;
	private WebRtcObject webRtcObject;
	private String roomCode;
	private Boolean streamEnabled;
}
