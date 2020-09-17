package com.car.auctionms.model.car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.car.auctionms.entity.CarEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
public class Car {

	private Integer id;
	
	@NotBlank(message="Car make must not be blank")
	private String make;
	@NotBlank(message="Car model must not be blank")
	private String model;
	@NotBlank(message="Car yaer must not be blank")
	private String year;
	@NotNull(message="Car km driven must not be blank")
	private Integer kmDriven;
	
	private String description;
	private String color;
	
	@Null(message="Car sale price must be null")
	private String salePrice;
	@Null(message="Car auction status must be null")
	private CarEntity.AuctionStatus auctionStatus;
	@Null(message="Car current auction status must be null")
	private CarEntity.CurrentAuctionStatus currentAuctionStatus;
	@Null(message="Car current auction id price must be null")
	private Integer currentAuctionId;
	@Null(message="Car highest bid price must be null")
	private Integer highestBidPrice;
	
}
