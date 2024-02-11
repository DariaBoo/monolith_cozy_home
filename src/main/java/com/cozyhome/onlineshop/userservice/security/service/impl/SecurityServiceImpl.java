package com.cozyhome.onlineshop.userservice.security.service.impl;

import com.cozyhome.onlineshop.userservice.model.UserStatusE;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.repository.UserRepository;
import com.cozyhome.onlineshop.userservice.security.service.SecurityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;

	@Override
	public boolean isAuthenticated(String username, String password) {
		try {
			log.info("[ON isAuthenticated]:: authenticating by UsernamePasswordAuthenticationToken...");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			log.info("[ON isAuthenticated]:: user with username [ {} ] is authenticated successfully", username);
			return true;
		} catch (AuthenticationException e) {
			log.error("[ON isAuthenticated]:: Exception :: {}.", e);
			throw new BadCredentialsException(e.getMessage());
		}
	}

	@Override
	public boolean isActivated(String username) {
		User user = userRepository.findByEmailAndStatus(username, UserStatusE.ACTIVE)
				.orElseThrow(() -> new DataNotFoundException("No user found by username " + username));
		return user.isActivated();
	}
}
