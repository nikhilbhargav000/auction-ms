package com.car.auctionms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car.auctionms.model.car.Bid;
import com.car.auctionms.model.car.Car;
import com.car.auctionms.model.response.CarAppsResponseMessage;
import com.car.auctionms.service.AuctionService;
import com.car.auctionms.service.BidService;
import com.car.auctionms.service.CarService;

@RestController
@RequestMapping(value="/admins")
public class AdminController {

	private CarService carService;
	private AuctionService auctionService;
	private BidService bidService;
	
	@Autowired
	public AdminController(CarService carService, AuctionService auctionService, BidService bidService) {
		this.carService = carService;
		this.auctionService = auctionService;
		this.bidService = bidService;
	}
	/**
	 * All cars view for Admin
	 * @param authentication
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/cars", method=RequestMethod.GET)
	public List<Car> findAllCars(Authentication authentication, @RequestParam(defaultValue="0") Integer page, 
			@RequestParam(defaultValue="20") Integer size) {
		List<Car> cars = carService.findAllCars(page, size, authentication);
		return cars;
	}
	
	/**
	 * All bids on a car
	 * @param authentication
	 * @param carId
	 * @return
	 */
	@RequestMapping(value="/cars/{carId}/bids", method=RequestMethod.GET)
	public List<Bid> findAllCarBids(Authentication authentication, @PathVariable(name = "carId") Integer carId) {
		List<Bid> bids = bidService.findBidsForCar(carId, authentication);
		return bids;
	}
	
	/**
	 * Create or recreate a Auction
	 * @param authentication
	 * @param carId
	 * @return
	 */
	@RequestMapping(value="/cars/{carId}/auctions/create", method=RequestMethod.GET)
	public CarAppsResponseMessage createCarAuction(Authentication authentication,
			@PathVariable(value = "carId") Integer carId) {
		CarAppsResponseMessage response = auctionService.createAuction(carId, authentication);
		return response;
	}
	
	/**
	 * Suspend a auction
	 * @param authentication
	 * @param auctionId
	 * @return
	 */
	@RequestMapping(value="/cars/auctions/{auctionId}/suspend", method=RequestMethod.GET)
	public CarAppsResponseMessage suspendCarAuction(Authentication authentication,
			@PathVariable(value = "auctionId") Integer auctionId) {
		CarAppsResponseMessage response = auctionService.suspendAuction(auctionId, authentication);
		return response;
	}
	
	/**
	 * Resume a auction
	 * @param authentication
	 * @param auctionId
	 * @return
	 */
	@RequestMapping(value="/cars/auctions/{auctionId}/resume", method=RequestMethod.GET)
	public CarAppsResponseMessage resumeCarAuction(Authentication authentication,
			@PathVariable(value = "auctionId") Integer auctionId) {
		CarAppsResponseMessage response = auctionService.resumeAuction(auctionId, authentication);
		return response;
	}
	
	/**
	 * Stop or end a auction
	 * @param authentication
	 * @param auctionId
	 * @return
	 */
	@RequestMapping(value="/cars/auctions/{auctionId}/stop", method=RequestMethod.GET)
	public CarAppsResponseMessage stopCarAuction(Authentication authentication,
			@PathVariable(value = "auctionId") Integer auctionId) {
		CarAppsResponseMessage response = auctionService.stopAuction(auctionId, authentication);
		return response;
	}

}
