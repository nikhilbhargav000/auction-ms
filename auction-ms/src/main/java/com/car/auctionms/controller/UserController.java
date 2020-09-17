package com.car.auctionms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.model.car.Car;
import com.car.auctionms.model.response.CarAppsResponseMessage;
import com.car.auctionms.service.CarService;

@RestController
@RequestMapping(value="users")
public class UserController {

	private CarService carService;
	
	@Autowired
	public UserController(CarService userService) {
		this.carService = userService;
	}

	/**
	 * User (Seller) add a car
	 * @param car
	 * @param authentication
	 * @param result
	 * @return
	 */
	@RequestMapping(value="/cars", method=RequestMethod.POST)
	public CarAppsResponseMessage addCar(@RequestBody @Valid Car car, Authentication authentication,
			BindingResult result) {
		if (result.hasErrors())
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().get(0).toString());
		
		CarAppsResponseMessage response = carService.addCar(car, authentication);
		return response;
	}
	
	/**
	 * Get all User (Seller) cars
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value="/cars", method=RequestMethod.GET)
	public List<Car> findAllCar(Authentication authentication) {
		List<Car> cars = carService.findAllSellerCars(authentication);
		return cars;
	}
	
	/**
	 * Get a User(Seller) car
	 * @param carId
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value="/cars/{carId}", method=RequestMethod.GET)
	public Car findCar(@PathVariable Integer carId, Authentication authentication) {
		Car car = carService.findCar(authentication, carId);
		return car;
	}
	
	/**
	 * Seller accept sale price
	 * @param carId
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value="/cars/{carId}/acceptBid", method=RequestMethod.GET)
	public CarAppsResponseMessage acceptSalePrice(@PathVariable Integer carId, Authentication authentication) {
		CarAppsResponseMessage response = carService.acceptSalePrice(carId, authentication);
		return response;
	}
	
	/**
	 * Seller reject sale price
	 * @param carId
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value="/cars/{carId}/rejectBid", method=RequestMethod.GET)
	public CarAppsResponseMessage rejectSalePrice(@PathVariable Integer carId, Authentication authentication) {
		CarAppsResponseMessage response = carService.rejectSalePrice(carId, authentication);
		return response;
	}
}
