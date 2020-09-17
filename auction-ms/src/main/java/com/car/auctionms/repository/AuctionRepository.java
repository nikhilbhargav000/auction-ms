package com.car.auctionms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.car.auctionms.entity.AuctionEntity;

public interface AuctionRepository extends JpaRepository<AuctionEntity, Integer> {

}
