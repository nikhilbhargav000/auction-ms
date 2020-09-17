package com.car.auctionms.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class LoginResponse extends CarAppsResponseMessage {

	private String jwtToken;
	
	public LoginResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	
}
