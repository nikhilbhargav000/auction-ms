package com.car.auctionms.exception;

import lombok.Getter;

@Getter
public class CarAppsException extends RuntimeException {

	private static final long serialVersionUID = 9020360072794148720L;

	private int responseCode;
	
	public CarAppsException(int responseCode, String message) {
		super(message);
		this.responseCode = responseCode;
	}
	
}
