package com.car.auctionms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="auctions")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
public class AuctionEntity {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator="auction_id_seq")
	private Integer id;

	@Column(name="seller_id")
	private String sellerId;
	
	@Column(name="car_id")
	private Integer carId;
	
}
