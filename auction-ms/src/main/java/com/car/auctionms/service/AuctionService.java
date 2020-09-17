package com.car.auctionms.service;

import org.springframework.security.core.Authentication;

import com.car.auctionms.model.response.CarAppsResponseMessage;

public interface AuctionService {
	
	public CarAppsResponseMessage createAuction(Integer carId, Authentication authentication) ;
	
	public CarAppsResponseMessage suspendAuction(Integer auctionId, Authentication authentication) ;
	
	public CarAppsResponseMessage resumeAuction(Integer auctionId, Authentication authentication) ;
	
	public CarAppsResponseMessage stopAuction(Integer auctionId, Authentication authentication) ;

}
