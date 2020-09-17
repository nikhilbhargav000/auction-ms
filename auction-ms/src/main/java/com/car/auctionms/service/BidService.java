package com.car.auctionms.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.car.auctionms.model.car.Bid;
import com.car.auctionms.model.response.CarAppsResponseMessage;

public interface BidService {

	public CarAppsResponseMessage addBid(Bid bid, Integer carId, Authentication authentication) ;
	
	public List<Bid> findUserBidsForCar(Integer carId, Authentication authentication) ;
	
	public List<Bid> findBidsForCar(Integer carId, Authentication authentication) ;
	
}
