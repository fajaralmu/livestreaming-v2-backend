package com.fajar.livestreaming.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fajar.livestreaming.dto.model.ApplicationConfigurationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application_configuration")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ApplicationConfiguration extends BaseEntity<ApplicationConfigurationModel> {/**
	 * 
	 */
	private static final long serialVersionUID = 8644941203309735869L;
	@Column(name="video_width", nullable = false)
	private int videoWidth;
	@Column(name="video_height", nullable = false)
	private int videoHeight;
	public static ApplicationConfiguration defaultInstance() {
		 
		ApplicationConfiguration config = ApplicationConfiguration.builder()
				.videoHeight(40).videoWidth(40).build();
		config.setCreatedDate(new Date());
		return config ;
	}

	
}
