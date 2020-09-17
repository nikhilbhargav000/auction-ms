package com.car.auctionms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.car.auctionms.entity.AuctionEntity;
import com.car.auctionms.entity.BidEntity;
import com.car.auctionms.entity.CarEntity;
import com.car.auctionms.entity.UserEntity;
import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.model.car.Bid;
import com.car.auctionms.model.response.CarAppsResponseMessage;
import com.car.auctionms.repository.AuctionRepository;
import com.car.auctionms.repository.BidRepository;
import com.car.auctionms.repository.CarRepository;
import com.car.auctionms.repository.UserRepository;
import com.car.auctionms.translator.BidTranslator;
import com.car.auctionms.util.AuthUtilities;

@Service
public class BidServiceImpl implements BidService {
	
	private BidRepository bidRepository;
	private CarRepository carRepository;
	private UserRepository userRepository;
	private AuctionRepository auctionRepository;
	private BidTranslator bidTranslator;
	private AuthUtilities authUtilities;
	
	@Value("${carsapp.minimumBidRaise}")
	private Integer minBidRaise;
	
	public BidServiceImpl(BidRepository bidRepository, CarRepository carRepository, UserRepository userRepository,
			AuctionRepository auctionRepository, BidTranslator bidTranslator, AuthUtilities authUtilities) {
		this.bidRepository = bidRepository;
		this.carRepository = carRepository;
		this.userRepository = userRepository;
		this.auctionRepository = auctionRepository;
		this.bidTranslator = bidTranslator;
		this.authUtilities = authUtilities;
	}
	
	@Retryable(maxAttempts=3, include=ObjectOptimisticLockingFailureException.class)
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public CarAppsResponseMessage addBid(Bid bid, Integer carId, Authentication authentication) {
		CarAppsResponseMessage response = null;
		authUtilities.isAuthenticatedUser(authentication);
		isValidCarId(carId);
		
		UserEntity buyerEntity = userRepository.findByEmail(authentication.getName());
		CarEntity carEntity = carRepository.findById(carId);
		
		if (carEntity.getCurrentAuctionStatus() == null
				|| !carEntity.getCurrentAuctionStatus().equals(CarEntity.CurrentAuctionStatus.ACTIVE)
				|| carEntity.getCurrentAuctionId() == null || carEntity.getAuctionStatus() == null
				|| !carEntity.getAuctionStatus().equals(CarEntity.AuctionStatus.CREATED))
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Invalid operation on car id");

		if (carEntity.getHighestBidPrice() + minBidRaise > bid.getPrice())
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), "Bid raise should be atleast " + minBidRaise);
		bid.setCarId(carId);
		bid.setUserId(buyerEntity.getUserId());
		
		AuctionEntity auctionEntity = auctionRepository.findById(carEntity.getCurrentAuctionId()).get();
		BidEntity bidEntity = bidTranslator.getBidEntity(bid, auctionEntity);
		
		bidEntity = bidRepository.saveAndFlush(bidEntity);
		carEntity.setHighestBidPrice(bidEntity.getPrice());
		carEntity.setHighestBid(bidEntity.getId());
		carRepository.saveAndFlush(carEntity);
		
		response = new CarAppsResponseMessage(HttpStatus.OK.value(), bidEntity.getId().toString());
		return response;
	}
	
	public List<Bid> findUserBidsForCar(Integer carId, Authentication authentication) {
		List<Bid> bids = new ArrayList<Bid>();
		
		authUtilities.isAuthenticatedUser(authentication);
		isValidCarId(carId);
		
		UserEntity userEntity = userRepository.findByEmail(authentication.getName());
		List<BidEntity> bidEntities = bidRepository.findAllByCarIdAndUserId(carId, userEntity.getUserId());

		bids = bidEntities.stream().map(bidEntity -> bidTranslator.getBid(bidEntity)).collect(Collectors.toList());
		return bids;
	}
	
	public List<Bid> findBidsForCar(Integer carId, Authentication authentication) {
		List<Bid> bids = new ArrayList<Bid>();
		authUtilities.isAuthenticatedAdmin(authentication);
		isValidCarId(carId);

		List<BidEntity> bidEntities = bidRepository.findAllByCarId(carId);

		bids = bidEntities.stream().map(bidEntity -> bidTranslator.getBid(bidEntity)).collect(Collectors.toList());
		return bids;
	}
	

	/////////////////////////////////////////////////////////
	/////////// Privates
	/////////////////////////////////////////////////////////
	
	private boolean isValidCarId(Integer carId) {
		boolean isValid = false;
		if (carId == null) 
			throw new CarAppsException(HttpStatus.NOT_FOUND.value(), "Invalid car id");
		CarEntity carEntity = carRepository.findById(carId);
		if (carEntity == null)
			throw new CarAppsException(HttpStatus.NOT_FOUND.value(), "Invalid car id");
		isValid = true;
		return isValid;
	}
}
