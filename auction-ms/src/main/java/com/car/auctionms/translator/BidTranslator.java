package com.car.auctionms.translator;

import org.springframework.stereotype.Component;

import com.car.auctionms.entity.AuctionEntity;
import com.car.auctionms.entity.BidEntity;
import com.car.auctionms.model.car.Bid;

@Component
public class BidTranslator {
	public BidEntity getBidEntity(Bid bid, AuctionEntity auctionEntity) {
		return BidEntity.builder()
				.id(bid.getId())
				.price(bid.getPrice())
				.carId(bid.getCarId())
				.userId(bid.getUserId())
				.auction(auctionEntity)
				.build();
	}
	
	public Bid getBid(BidEntity bidEntity) {
		return Bid.builder()
				.id(bidEntity.getId())
				.price(bidEntity.getPrice())
				.carId(bidEntity.getCarId())
				.userId(bidEntity.getUserId())
				.build();
	}
}
