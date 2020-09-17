package com.car.auctionms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.model.car.Bid;
import com.car.auctionms.model.car.Car;
import com.car.auctionms.model.response.CarAppsResponseMessage;
import com.car.auctionms.service.BidService;
import com.car.auctionms.service.CarService;

@RestController
@RequestMapping("cars")
public class CarController {

	private CarService carService;
	private BidService bidService;
	
	@Autowired
	public CarController(CarService carService, BidService bidService) {
		this.carService = carService;
		this.bidService = bidService;
	}
	
	/**
	 * User(Buyer) pagination fetch of all non-suspended cars
	 * @param authentication
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public List<Car> findAllNonSuspendedCars(Authentication authentication, @RequestParam(defaultValue="0") Integer page, 
			@RequestParam(defaultValue="20") Integer size) {
		List<Car> cars = carService.findAllNonSuspendedCars(page, size, authentication);
		return cars;
	}

	/**
	 * User(Buyer) bid on a car
	 * @param bid
	 * @param carId
	 * @param result
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/{carId}/bids", method = RequestMethod.POST)
	public CarAppsResponseMessage addBid(@RequestBody Bid bid, @PathVariable("carId") Integer carId,
			BindingResult result, Authentication authentication) {
		if (result.hasErrors())
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().get(0).toString());
		
		CarAppsResponseMessage response = bidService.addBid(bid, carId, authentication);
		return response;
	}
	
	/**
	 * User(Buyer) can fetch his bids on a car
	 * @param bid
	 * @param carId
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/{carId}/bids", method = RequestMethod.GET)
	public List<Bid> findUserBidsForCar(@PathVariable("carId") Integer carId,
			Authentication authentication) {
		List<Bid> bids = bidService.findUserBidsForCar(carId, authentication);
		return bids;
	}
	
}
