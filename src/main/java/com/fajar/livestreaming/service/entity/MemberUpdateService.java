package com.fajar.livestreaming.service.entity;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.constants.AuthorityType;
import com.fajar.livestreaming.entity.Authority;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.exception.ApplicationException;
import com.fajar.livestreaming.repository.AuthorityRepository;
import com.fajar.livestreaming.service.SessionValidationService;

@Service
public class MemberUpdateService extends BaseEntityUpdateService<User>{

	static final String DEFAULT_PASSWORD = "secret";
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private SessionValidationService sessionValidationService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Override
	public User saveEntity(User object, boolean newRecord, HttpServletRequest httpServletRequest) throws Exception {
		 
		User loggedUser = sessionValidationService.getLoggedUser(httpServletRequest);
		if (!loggedUser.isAdmin()) {
			throw new ApplicationException("Not allowed");
		}
		validateEntityFormFields(object, newRecord, httpServletRequest);
		
		/**
		 * password
		 */
		boolean passwordNotSpecified = null == object.getPassword() || "".equals(object.getPassword().trim());
		if (newRecord) {
			if (passwordNotSpecified) {
				object.setPassword(DEFAULT_PASSWORD);
			}
			String encodedPassword = passwordEncoder.encode(object.getPassword());
			object.setPassword(encodedPassword);
			object.setAuthorities(getMemberAuthorities(object.getMainRole()));
		} else {
			User existingObject = entityRepository.findById(User.class, object.getId());
			if (passwordNotSpecified) {
				object.setPassword(existingObject.getPassword());
			}
			String encodedPassword = passwordEncoder.encode(object.getPassword());
			if (existingObject.getPassword().equals(object.getPassword())) {
				//password not updated
			} else {
				object.setPassword(encodedPassword);
			}
			
			/**
			 * Roles check, currently not supporting for multiple roles
			 */
			Set<Authority> memberRoles = getMemberAuthorities(object.getMainRole());
			if (existingObject.hasAuthority(object.getMainRole())) {
				object.setAuthorities(existingObject.getAuthorities());
			} else {
				object.setAuthorities(memberRoles);
			}
			 
		}
		object.prePersist();
		return entityRepository.save(object);
	}
	
	
	private Set<Authority> getMemberAuthorities(AuthorityType type) {
		Authority roleUser = authorityRepository.findTop1ByName(type);
		Set<Authority> authorities = new HashSet<>();
		authorities.add(roleUser);
		return authorities ;
	}
}
