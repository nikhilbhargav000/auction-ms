package com.car.auctionms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.car.auctionms.entity.CarEntity;
import com.car.auctionms.entity.UserEntity;
import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.model.car.Car;
import com.car.auctionms.model.response.CarAppsResponseMessage;
import com.car.auctionms.repository.CarRepository;
import com.car.auctionms.repository.UserRepository;
import com.car.auctionms.translator.CarTranslator;
import com.car.auctionms.util.AuthUtilities;

@Service
public class CarServiceImpl implements CarService {
	
	private CarTranslator carTranslator;
	private UserRepository userRepository;
	private CarRepository carRepository;
	private AuthUtilities authUtilities;
	
	@Autowired
	public CarServiceImpl(CarTranslator carTranslator, UserRepository userRepository, 
			CarRepository carRepository, AuthUtilities authUtilities) {
		this.carTranslator = carTranslator;
		this.userRepository = userRepository;
		this.carRepository = carRepository;
		this.authUtilities = authUtilities;
	}
	
	public CarAppsResponseMessage addCar(Car car, Authentication authentication) {
		CarAppsResponseMessage response = null;
		
		authUtilities.isAuthenticatedUser(authentication);
		
		UserEntity userEntity = userRepository.findByEmail(authentication.getName());

		CarEntity carEntity = carTranslator.getCarEntity(car, userEntity);
		carEntity.setDate(new Date());
		carEntity.setAuctionStatus(CarEntity.AuctionStatus.TO_CREATE); 
		carEntity.setCurrentAuctionStatus(CarEntity.CurrentAuctionStatus.ACTIVE); 
		carEntity.setHighestBidPrice(0);
		carRepository.saveAndFlush(carEntity);
		
		response = new CarAppsResponseMessage(HttpStatus.OK.value(), carEntity.getId().toString());
		return response;
	}
	
	public List<Car> findAllSellerCars(Authentication authentication) {
		List<Car> cars = null;
		
		authUtilities.isAuthenticatedUser(authentication);
		UserEntity userEntity = userRepository.findByEmail(authentication.getName());
		List<CarEntity> carEntities = carRepository.findBySeller(userEntity);
		cars = carEntities.stream().map(carEntity -> carTranslator.getCar(carEntity))
				.collect(Collectors.toList());
		
		return cars;
	}
	
	public Car findCar(Authentication authentication, Integer carId) {
		Car car = null;
		
		authUtilities.isAuthenticatedUser(authentication);
		isValidCarId(carId, authentication);
		
		CarEntity carEntity = carRepository.findById(carId);
		
		car = carTranslator.getCar(carEntity);
		return car;
	}
	
	public List<Car> findAllNonSuspendedCars(int page, int size, Authentication authentication) {
		List<Car> cars = new ArrayList<Car>();

		Page<CarEntity> carEntitiesPage = carRepository.findAllByCurrentAuctionStatusIn(
				PageRequest.of(page, size, Sort.by(Direction.DESC, "date")),
				Arrays.asList(CarEntity.CurrentAuctionStatus.ACTIVE));
		if (carEntitiesPage == null || carEntitiesPage.getSize() < 1)
			return cars;
		
		cars = carEntitiesPage.toList().stream().map(carEntity -> carTranslator.getCar(carEntity))
				.collect(Collectors.toList());
		return cars;
	}
	
	public List<Car> findAllCars(int page, int size, Authentication authentication) {
		List<Car> cars = new ArrayList<Car>();
		
		authUtilities.isAuthenticatedAdmin(authentication);
		
		Page<CarEntity> carEntitiesPage = carRepository.findAll(PageRequest.of(page, size, Sort.by(Direction.DESC, "date")));
		if (carEntitiesPage == null || carEntitiesPage.getSize() < 1)
			return cars;
		
		cars = carEntitiesPage.toList().stream().map(carEntity -> carTranslator.getCar(carEntity))
				.collect(Collectors.toList());
		return cars;
	}

	public CarAppsResponseMessage acceptSalePrice(Integer carId, Authentication authentication) {
		CarAppsResponseMessage response = null;
		authUtilities.isAuthenticatedUser(authentication);
		isValidCarId(carId, authentication);

		CarEntity carEntity = carRepository.findById(carId);
		
		if (carEntity.getAuctionStatus() == null
				|| !carEntity.getAuctionStatus().equals(CarEntity.AuctionStatus.ENDED)
				|| carEntity.getSalePrice() == null)
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid operation");
		
		carEntity.setAuctionStatus(CarEntity.AuctionStatus.SOLD);
		carRepository.saveAndFlush(carEntity);
		
		return response;
	}

	public CarAppsResponseMessage rejectSalePrice(Integer carId, Authentication authentication) {
		CarAppsResponseMessage response = null;
		authUtilities.isAuthenticatedUser(authentication);
		isValidCarId(carId, authentication);
		
		CarEntity carEntity = carRepository.findById(carId);
		
		if (carEntity.getAuctionStatus() == null
				|| !carEntity.getAuctionStatus().equals(CarEntity.AuctionStatus.ENDED))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid operation");
		
		carEntity.setAuctionStatus(CarEntity.AuctionStatus.REJECTED);
		carRepository.saveAndFlush(carEntity);
		
		
		return response;
	}
	
	/////////////////////////////////////////////////////////
	/////////// Privates
	/////////////////////////////////////////////////////////
	
	private boolean isValidCarId(Integer carId, Authentication authentication) {
		boolean isValid = false;
		if (carId == null)
			throw new CarAppsException(HttpStatus.NOT_FOUND.value(), "Invalid car id");
		
		CarEntity carEntity = carRepository.findById(carId);
		
		if (carEntity == null)
			throw new CarAppsException(HttpStatus.NOT_FOUND.value(), "Invalid car id");

		UserEntity userEntity = userRepository.findByEmail(authentication.getName());
		if (!carEntity.getSeller().getUserId().equals(userEntity.getUserId())) 
			throw new CarAppsException(HttpStatus.FORBIDDEN.value(), "Forbidden access");
		isValid = true;
		return isValid;
	}
	
}
