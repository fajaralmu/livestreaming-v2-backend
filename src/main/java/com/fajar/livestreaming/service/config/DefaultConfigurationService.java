package com.fajar.livestreaming.service.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.entity.ApplicationConfiguration;
import com.fajar.livestreaming.repository.ApplicationConfigurationRepository;

@Service
public class DefaultConfigurationService {

	@Autowired
	private ApplicationConfigurationRepository applicationConfigurationRepository;
	private ApplicationConfiguration applicationConfiguration;
	@PostConstruct
	public void init() {
		checkConfig();
	}
	
	private void checkConfig() {
		applicationConfiguration = applicationConfigurationRepository.findTop1();
		if (null == applicationConfiguration) {
			applicationConfiguration = (ApplicationConfiguration.defaultInstance());
			ApplicationConfiguration saved = applicationConfigurationRepository.save(applicationConfiguration);
			applicationConfiguration = (saved);
		}
	}

	
	public ApplicationConfiguration getAndCheckApplicationConfiguration() {
	 
		checkConfig(); 
		return applicationConfiguration;
	}
	public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
		this.applicationConfiguration = applicationConfiguration;
	}
	
}
