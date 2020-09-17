package com.car.auctionms.model.car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.car.auctionms.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
public class User implements UserDetails {

	private static final long serialVersionUID = 4541028345066965205L;
	
	private String userId;
	private String name;
	private String email;
	private String password;
	private UserEntity.Role role;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final String rolePrefix = "ROLE_";
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if (StringUtils.isBlank(this.role.name()))
			return authorities;
		authorities.add(new SimpleGrantedAuthority(rolePrefix + this.role.name()));
		return authorities;
	}
	@Override
	public String getPassword() {
		return this.password;
	}
	@Override
	public String getUsername() {
		return this.email;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
