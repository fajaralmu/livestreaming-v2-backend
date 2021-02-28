package com.fajar.livestreaming.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fajar.livestreaming.constants.AuthorityType;
import com.fajar.livestreaming.dto.model.AuthorityModel;
import com.fajar.livestreaming.dto.model.UserModel;
import com.fajar.livestreaming.entity.setting.SingleImageModel;
import com.fajar.livestreaming.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Table(name = "users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<UserModel> implements SingleImageModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3896877759244837620L;
	
	@Column(unique = true, nullable = false) 
	private String code;
	@Column(unique = true, nullable = false) 
	private String username;
	@Column(name = "display_name") 
	private String displayName;
	@Column
	private String password;
 
	@Column(name = "profile_image")
	private String profileImage;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER) 
	@JoinTable(name = "user_authority", 
			joinColumns = { @JoinColumn(name = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "authority_id") }) 
	@Default
	private Set<Authority> authorities = new HashSet<>();

	public void addAuthority(Authority authority) {
		authorities.add(authority);
	}
	 
	@Transient
	@JsonIgnore
	private String requestId;

	@Transient
	@JsonIgnore
	private Date processingDate; // for transaction
	@Transient
	private AuthorityType mainRole;
	
	@Override
	public void setImage(String image) {
		this.profileImage = image;
	}

	@Override
	public String getImage() {
		return this.profileImage;
	}

	public boolean isAdmin() {
		return hasAuthority(AuthorityType.ROLE_ADMIN) ;
	}

	public boolean hasAuthority(AuthorityType authorityType) {
		if (null == authorities) return false;
		for (Authority authority : authorities) {
			if (authority!=null && authority.getName().equals(authorityType)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public UserModel toModel() {
		
		UserModel user = copy(getModelInstance(), "authorities");
		Set<AuthorityModel> _authorities = new HashSet<AuthorityModel>();
		this.authorities.forEach(a->{_authorities.add(a.toModel());});
		user.setAuthorities(_authorities );
		return user;
	}
	
	@PrePersist
	@Override
	public void prePersist() {
		super.prePersist();
		if (null == code) {
			code = StringUtil.generateRandomNumber(10);
		}
	}

	public void encodePassword(BCryptPasswordEncoder passwordEncoder) {
		 setPassword(passwordEncoder.encode(getPassword()));
	}
	 

}
