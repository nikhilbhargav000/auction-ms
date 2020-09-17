package com.car.auctionms.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.car.auctionms.model.car.Car;
import com.car.auctionms.model.response.CarAppsResponseMessage;

public interface CarService {
	
	public CarAppsResponseMessage addCar(Car car, Authentication authentication) ;
	
	public List<Car> findAllSellerCars(Authentication authentication) ;
	
	public Car findCar(Authentication authentication, Integer carId) ;
	
	public List<Car> findAllNonSuspendedCars(int page, int size, Authentication authentication) ;
	
	public List<Car> findAllCars(int page, int size, Authentication authentication) ;

	public CarAppsResponseMessage acceptSalePrice(Integer carId, Authentication authentication) ;

	public CarAppsResponseMessage rejectSalePrice(Integer carId, Authentication authentication) ;
	
	
}
