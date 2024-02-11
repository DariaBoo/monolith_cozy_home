package com.cozyhome.onlineshop.userservice.security.service.impl;

import java.util.Optional;

import com.cozyhome.onlineshop.userservice.model.UserStatusE;
import com.cozyhome.onlineshop.userservice.security.AuthenticatedUserDetails;
import com.cozyhome.onlineshop.userservice.security.service.ExtendedUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements ExtendedUserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public AuthenticatedUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmailAndStatus(email, UserStatusE.ACTIVE);
		if(user.isEmpty()) {
			log.warn("[ON loadUserByUsername]:: user with username::[ {} ] not found", email);
            throw new UsernameNotFoundException("User not found for username - " + email);
		}
		log.info("[ON loadUserByUsername]:: loaded user with username [ {} ] and roles [ {} ]", user.get().getEmail(),
                user.get().getRoles());
        return new AuthenticatedUserDetails(user.get());
	}		

}
