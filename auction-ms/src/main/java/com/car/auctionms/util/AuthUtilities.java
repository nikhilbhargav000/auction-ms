package com.car.auctionms.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.car.auctionms.entity.UserEntity;
import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.repository.UserRepository;


@Component
public class AuthUtilities {
	
	private final UserRepository userRepository;
	
	@Autowired
	public AuthUtilities(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	

	public boolean isAuthenticatedAdmin(Authentication authentication) {
		String authEmail = authentication.getName();
		if (StringUtils.isBlank(authEmail))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid authentication email");

		UserEntity authUserEntity = userRepository.findByEmail(authEmail);
		if (authUserEntity == null || StringUtils.isBlank(authUserEntity.getUserId()) ||
				!authUserEntity.getRole().equals(UserEntity.Role.ADMIN))
			throw new CarAppsException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
		return true;

	}
	
	public boolean isAuthenticatedUser(Authentication authentication) {
		String authEmail = authentication.getName();
		if (StringUtils.isBlank(authEmail))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid authentication email");

		UserEntity authUserEntity = userRepository.findByEmail(authEmail);
		if (authUserEntity == null || StringUtils.isBlank(authUserEntity.getUserId()))
			throw new CarAppsException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
		return true;
	}
	
}
