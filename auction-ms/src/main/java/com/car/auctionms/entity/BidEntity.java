package com.car.auctionms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="bids")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
@IdClass(BidIdEntity.class)
public class BidEntity {

	@Column(name="car_id")
	private Integer carId;

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator="bid_id_seq")
	private Integer id;
	
	private Integer price;

	@Column(name="user_id")
	private String userId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="auction_id")
	private AuctionEntity auction;
	
}
