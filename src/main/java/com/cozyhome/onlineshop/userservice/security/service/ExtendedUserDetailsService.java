package com.cozyhome.onlineshop.userservice.security.service;

import com.cozyhome.onlineshop.userservice.security.AuthenticatedUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ExtendedUserDetailsService extends UserDetailsService {
    AuthenticatedUserDetails loadUserByUsername(String email);
}
