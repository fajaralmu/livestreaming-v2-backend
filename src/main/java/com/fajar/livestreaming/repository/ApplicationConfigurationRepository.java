package com.fajar.livestreaming.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fajar.livestreaming.entity.ApplicationConfiguration;

public interface ApplicationConfigurationRepository extends JpaRepository<ApplicationConfiguration, Long> {

	@Query("select c from ApplicationConfiguration c order by id desc")
	List<ApplicationConfiguration> findPaginated(Pageable pageable);
 
	default ApplicationConfiguration findTop1() {
		List<ApplicationConfiguration> list = findPaginated(PageRequest.of(0, 1));
		if (list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
