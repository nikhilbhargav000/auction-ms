package com.car.auctionms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.car.auctionms.entity.AuctionEntity;
import com.car.auctionms.entity.CarEntity;
import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.model.response.CarAppsResponseMessage;
import com.car.auctionms.repository.AuctionRepository;
import com.car.auctionms.repository.CarRepository;
import com.car.auctionms.util.AuthUtilities;

@Service
public class AuctionServiceImpl implements AuctionService {

	private CarRepository carRepository;
	private AuctionRepository auctionRepository;
	private AuthUtilities authUtilities;
	
	@Autowired
	public AuctionServiceImpl(CarRepository carRepository, AuthUtilities authUtilities, AuctionRepository auctionRepository) {
		this.carRepository = carRepository;
		this.auctionRepository = auctionRepository;
		this.authUtilities = authUtilities;
	}
	
	@Retryable(maxAttempts=3, include=ObjectOptimisticLockingFailureException.class)
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public CarAppsResponseMessage createAuction(Integer carId, Authentication authentication) {
		CarAppsResponseMessage response = null;
		authUtilities.isAuthenticatedAdmin(authentication);
		
		if (carId == null)
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid car id");
		
		CarEntity carEntity = carRepository.findById(carId);
		if (carEntity == null || carEntity.getAuctionStatus() == null
				|| !(carEntity.getAuctionStatus().equals(CarEntity.AuctionStatus.TO_CREATE)
						|| carEntity.getAuctionStatus().equals(CarEntity.AuctionStatus.REJECTED)))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid operation on provided car id");
		
		AuctionEntity auctionEntity = AuctionEntity.builder()
				.sellerId(carEntity.getSeller().getUserId())
				.carId(carEntity.getId())
				.build();
		auctionRepository.saveAndFlush(auctionEntity);
		carEntity.setAuctionStatus(CarEntity.AuctionStatus.CREATED);
		carEntity.setCurrentAuctionStatus(CarEntity.CurrentAuctionStatus.ACTIVE);
		carEntity.setCurrentAuctionId(auctionEntity.getId());
		carRepository.saveAndFlush(carEntity);
		
		response = new CarAppsResponseMessage(HttpStatus.OK.value(), auctionEntity.getId().toString());
		
		return response;
	}
	
	@Retryable(maxAttempts=3, include=ObjectOptimisticLockingFailureException.class)
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public CarAppsResponseMessage suspendAuction(Integer auctionId, Authentication authentication) {
		CarAppsResponseMessage response = null;

		authUtilities.isAuthenticatedAdmin(authentication);
		isValidAuctionId(auctionId);
		
		AuctionEntity auctionEntity = auctionRepository.findById(auctionId).get();
		CarEntity carEntity = carRepository.findById(auctionEntity.getCarId());

		if (carEntity.getCurrentAuctionStatus() == null
				|| carEntity.getCurrentAuctionStatus().equals(CarEntity.CurrentAuctionStatus.SUSPENDED))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid operation for given auction id");
		
		carEntity.setCurrentAuctionStatus(CarEntity.CurrentAuctionStatus.SUSPENDED);
		carRepository.saveAndFlush(carEntity);
		
		response = new CarAppsResponseMessage(HttpStatus.OK.value(), "Success");
		return response;
	}
	
	@Retryable(maxAttempts=3, include=ObjectOptimisticLockingFailureException.class)
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public CarAppsResponseMessage resumeAuction(Integer auctionId, Authentication authentication) {
		CarAppsResponseMessage response = null;

		authUtilities.isAuthenticatedAdmin(authentication);
		isValidAuctionId(auctionId);
		
		AuctionEntity auctionEntity = auctionRepository.findById(auctionId).get();
		CarEntity carEntity = carRepository.findById(auctionEntity.getCarId());

		if (carEntity.getCurrentAuctionStatus() == null
				|| carEntity.getCurrentAuctionStatus().equals(CarEntity.CurrentAuctionStatus.ACTIVE))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid operation for given auction id");

		carEntity.setCurrentAuctionStatus(CarEntity.CurrentAuctionStatus.ACTIVE);
		carRepository.saveAndFlush(carEntity);
		
		response = new CarAppsResponseMessage(HttpStatus.OK.value(), "Success");
		return response;
	}
	
	@Retryable(maxAttempts=3, include=ObjectOptimisticLockingFailureException.class)
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public CarAppsResponseMessage stopAuction(Integer auctionId, Authentication authentication) {
		CarAppsResponseMessage response = null;
		
		authUtilities.isAuthenticatedAdmin(authentication);
		isValidAuctionId(auctionId);
		
		AuctionEntity auctionEntity = auctionRepository.findById(auctionId).get();
		CarEntity carEntity = carRepository.findById(auctionEntity.getCarId());

		if (carEntity.getCurrentAuctionStatus() == null
				|| !carEntity.getAuctionStatus().equals(CarEntity.AuctionStatus.CREATED))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid operation for given auction id");
		
		carEntity.setAuctionStatus(CarEntity.AuctionStatus.ENDED);
		if (carEntity.getHighestBidPrice() != null)
			carEntity.setSalePrice(carEntity.getHighestBidPrice().toString());
		carRepository.saveAndFlush(carEntity);
		
		response = new CarAppsResponseMessage(HttpStatus.OK.value(), "Success");
		return response;
	}
	
	/////////////////////////////////////////////////////////
	/////////// Privates
	/////////////////////////////////////////////////////////
	
	public boolean isValidAuctionId(Integer auctionId) {
		boolean isValid = false;
		
		if (auctionId == null)
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid auctionId id");
		
		AuctionEntity auctionEntity = auctionRepository.findById(auctionId).get();
		
		if (auctionEntity == null)
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid auctionId id");
		
		isValid = true;
		return isValid;
	}
	
}
