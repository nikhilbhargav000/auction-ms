package com.car.auctionms.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.car.auctionms.util.JwtUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public final String AUTH_HEADER_KEY = "authentication";
	public final String AUTH_HEADER_PREFEX = "bearer ";
	public final String REDIRECT_URL = "http://localhost:8080/login/";

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	
	@Autowired
	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		super();
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Optional<String> userNameOpt = fetchUserName(request);
		
		UserDetails userDetails = null;
		if (userNameOpt.isPresent() && (userDetails = userDetailsService.loadUserByUsername(userNameOpt.get())) != null
				&& SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UsernamePasswordAuthenticationToken userSecurityToken = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(userSecurityToken);
		}
		doFilter(request, response, filterChain);
	}
	
	private Optional<String> fetchUserName(HttpServletRequest request) {
		Optional<String> userNameOpt = Optional.empty();
		
		String token = request.getHeader(AUTH_HEADER_KEY);
		if (StringUtils.isBlank(token) || !token.startsWith(AUTH_HEADER_PREFEX)
				|| token.length() <= AUTH_HEADER_PREFEX.length())
			return userNameOpt;
		token = token.substring(AUTH_HEADER_PREFEX.length());
		
		if (!jwtUtil.isTokenExpired(token))
			userNameOpt = jwtUtil.getDecodedUserName(token);
		
		return userNameOpt;
	}

}
