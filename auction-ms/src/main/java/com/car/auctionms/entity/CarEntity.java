package com.car.auctionms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="cars")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
@IdClass(CarIdEntity.class)
public class CarEntity {

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="seller_id")
	private UserEntity seller;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="car_id_seq")
	private Integer id;
	
	private String make;
	private String model;
	private String year;
	@Column(name="km_driven")
	private Integer kmDriven;
	
	private String description;
	private String color;
	
	@Column(name="sale_price")
	private String salePrice;

	private AuctionStatus auctionStatus;
	private CurrentAuctionStatus currentAuctionStatus;

	@Column(name="current_auction_id")
	private Integer currentAuctionId;

	@Column(name="highest_bid_id")
	private Integer highestBid;
	
	private Integer highestBidPrice;
	
	private Date date;
	
	@Version
	private Integer version;
	
	
	/**
	 * Auction Status values for Car table
	 * @author nikhil
	 *
	 */
	public static enum AuctionStatus {
		TO_CREATE, 
		CREATED, 
		ENDED,
		REJECTED,
		SOLD;
	}
	
	/**
	 * Auction Status values for Auction table
	 * @author nikhil
	 *
	 */
	public static enum CurrentAuctionStatus {
		ACTIVE,
		SUSPENDED
	}
}



