package com.car.auctionms.util;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

	@Value("${carsapp.jwtSecret}")
	private String SECRET_KEY;

	@Value("${carsapp.jwtExpirationMs}")
	private long JWT_EXPIRATIONS_MS;
	
	private Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	public boolean isTokenExpired(String token) {
		boolean isExpired = true;
		DecodedJWT jwtToken = getJwtToken(token);
		if (jwtToken != null && jwtToken.getExpiresAt().after(new Date(System.currentTimeMillis())))
			isExpired = false;
		return isExpired;
	}
	
	/**
	 * Return valid UserName from token (if not expired)
	 * @param token
	 * @return
	 */
	public Optional<String> getDecodedUserName(String token) { 
		Optional<String> userNameOpt = Optional.empty();
		
		DecodedJWT jwtToken = getJwtToken(token);
		if (jwtToken != null && jwtToken.getExpiresAt().after(new Date(System.currentTimeMillis()))) 
			userNameOpt = Optional.of(jwtToken.getSubject());
		
		return userNameOpt;
	}
	
	public String generateJwtToken(UserDetails userDetails) {
		return generateJwtToken(userDetails.getUsername());
	}
	
	public String generateJwtToken(String subject) {
		if (StringUtils.isEmpty(subject)) {
			new IllegalArgumentException("Invalid username to generate jwt token");
		}
		return JWT.create()
			.withSubject(subject)
			.withClaim("Test", "Test")
			.withIssuedAt(new Date(System.currentTimeMillis()))
			.withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATIONS_MS))
			.sign(Algorithm.HMAC256(SECRET_KEY));
	}
	
	private DecodedJWT getJwtToken(String token) {
		DecodedJWT jwtToken = null;
		try {
			jwtToken = JWT.decode(token);
		} catch (Exception e) {
			logger.info(e.getStackTrace().toString());
		}
		return  jwtToken;
	}
	
}
