package com.car.auctionms.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.car.auctionms.entity.UserEntity;
import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.model.car.User;
import com.car.auctionms.model.request.Login;
import com.car.auctionms.model.response.LoginResponse;
import com.car.auctionms.repository.UserRepository;
import com.car.auctionms.translator.UserTranslator;
import com.car.auctionms.util.JwtUtil;

@Service
public class CarAppsUserDetailsServiceImpl implements UserDetailsService, UserService {

	private final UserRepository userRepository;
	private final UserTranslator userTranslator;
	private final JwtUtil jwtUtil;
	
	public CarAppsUserDetailsServiceImpl(UserRepository userRepository, UserTranslator userTranslator,
			JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.userTranslator = userTranslator;
		this.jwtUtil = jwtUtil;
	}
	
	public LoginResponse login(Login login) {
		LoginResponse response = null;
		
		UserEntity userEntity = userRepository.findByEmail(login.getUserEmail());
		if (userEntity == null || !login.getPassword().equals(userEntity.getPassword()))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Please check your credencials");
		String jwtToken = jwtUtil.generateJwtToken(userEntity.getEmail());
		
		response = new LoginResponse(jwtToken);
		return response;
	}
	
	public User getUserByEmail(String email) throws CarAppsException {
		if (StringUtils.isBlank(email)) 
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid email");
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) 
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid email");

		return userTranslator.getUser(userEntity);
	}
	
	/**
	 * For Spring Security
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		try {
			user = getUserByEmail(username);
		} catch (CarAppsException e) {
			throw new UsernameNotFoundException("Invalid username");
		}
		return user;
	}
	
}
