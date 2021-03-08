package com.fajar.livestreaming.dto.model;

import com.fajar.livestreaming.annotation.Dto;
import com.fajar.livestreaming.annotation.FormField;
import com.fajar.livestreaming.constants.FieldType;
import com.fajar.livestreaming.entity.ApplicationConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Dto( updateService = "configurationUpdateService", creatable = false)
@Builder
@Data
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class ApplicationConfigurationModel extends BaseModel<ApplicationConfiguration> { 
	/**
	 * 
	 */
	private static final long serialVersionUID = -340379216122694021L;
	@FormField(type = FieldType.FIELD_TYPE_NUMBER)
	private int videoWidth;
	@FormField(type = FieldType.FIELD_TYPE_NUMBER)
	private int videoHeight;

	
}
