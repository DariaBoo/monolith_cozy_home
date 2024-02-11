package com.cozyhome.onlineshop.userservice.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.model.UserStatusE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthenticatedUserDetails implements UserDetails {
	
	private User user;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = user.getRoles()
				.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).toList();
		return authorities;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return user.getStatus().equals(UserStatusE.BLOCKED) ? false : true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.getStatus().equals(UserStatusE.ACTIVE) ? true : false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getStatus().equals(UserStatusE.ACTIVE) ? true : false;
	}

}
