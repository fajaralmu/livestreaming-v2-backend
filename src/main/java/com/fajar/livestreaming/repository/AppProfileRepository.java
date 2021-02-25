package com.fajar.livestreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.livestreaming.entity.ApplicationProfile;

public interface AppProfileRepository extends JpaRepository<ApplicationProfile, Long> {
 

	ApplicationProfile findByAppCode(String appCode); 

}
