package com.fajar.livestreaming.service.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.entity.ApplicationConfiguration;
import com.fajar.livestreaming.exception.DataNotFoundException;
import com.fajar.livestreaming.service.config.DefaultConfigurationService;

@Service
public class ConfigurationUpdateService  extends BaseEntityUpdateService<ApplicationConfiguration>{

	@Autowired
	private DefaultConfigurationService DefaultConfigurationService;
	 
	@Override
	public ApplicationConfiguration saveEntity(ApplicationConfiguration object, boolean newRecord,
			HttpServletRequest httpServletRequest) throws Exception {
		if (object.getId() == null) {
			throw new DataNotFoundException("Config NOT FOUND");
		}
		validateEntityFormFields(object, false, httpServletRequest);
		ApplicationConfiguration saved = entityRepository.save(object);
		DefaultConfigurationService.setApplicationConfiguration(saved);
		return saved;
	}
}
