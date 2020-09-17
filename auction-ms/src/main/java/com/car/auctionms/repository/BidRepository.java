package com.car.auctionms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.car.auctionms.entity.BidEntity;
import com.car.auctionms.entity.BidIdEntity;

public interface BidRepository extends JpaRepository<BidEntity, BidIdEntity> {

	public List<BidEntity> findAllByCarIdAndUserId(Integer carId, String userId);
	
	public List<BidEntity> findAllByCarId(Integer carId);
	
}
