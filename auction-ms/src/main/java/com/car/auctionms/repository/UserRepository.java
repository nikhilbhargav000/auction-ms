package com.car.auctionms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.car.auctionms.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	
	public UserEntity findByEmail(String email) ;
	
}
