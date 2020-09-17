package com.car.auctionms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.car.auctionms.entity.CarEntity;
import com.car.auctionms.entity.CarIdEntity;
import com.car.auctionms.entity.UserEntity;

public interface CarRepository extends JpaRepository<CarEntity, CarIdEntity> {

	public CarEntity findById(Integer id);
	
	public List<CarEntity> findBySeller(UserEntity userEntity);

	public Page<CarEntity> findAllByCurrentAuctionStatusIn(Pageable pageable,
			List<CarEntity.CurrentAuctionStatus> currentAuctionStatuses);
	
	
}
