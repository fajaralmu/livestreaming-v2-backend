package com.fajar.livestreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.livestreaming.constants.AuthorityType;
import com.fajar.livestreaming.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findTop1ByName(AuthorityType type);
 

}
