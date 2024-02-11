package com.cozyhome.onlineshop.userservice.security.service.impl;

import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.userservice.model.TokenBlackList;
import com.cozyhome.onlineshop.userservice.repository.TokenBlackListRepository;
import com.cozyhome.onlineshop.userservice.security.service.TokenBlackListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {
	private final TokenBlackListRepository tokenBlackListRepository;

	@Override
	public void saveTokenToBlackList(String jwtToken) {
		tokenBlackListRepository.save(new TokenBlackList(jwtToken));
	}
}
