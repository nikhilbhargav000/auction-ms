package com.car.auctionms.translator;

import org.springframework.stereotype.Component;

import com.car.auctionms.entity.CarEntity;
import com.car.auctionms.entity.UserEntity;
import com.car.auctionms.model.car.Car;

@Component
public class CarTranslator {

	public CarEntity getCarEntity(Car car, UserEntity seller) {
		return CarEntity.builder()
			.id(car.getId())
			.make(car.getMake())
			.model(car.getModel())
			.year(car.getYear())
			.kmDriven(car.getKmDriven())
			.description(car.getDescription())
			.color(car.getColor())
			.salePrice(car.getSalePrice())
			.auctionStatus(car.getAuctionStatus())
			.highestBidPrice(car.getHighestBidPrice())
			.seller(seller)
			.currentAuctionStatus(car.getCurrentAuctionStatus())
			.currentAuctionId(car.getCurrentAuctionId())
			.build();
	}
	
	public Car getCar(CarEntity carEntity) {
		return Car.builder()
			.id(carEntity.getId())
			.make(carEntity.getMake())
			.model(carEntity.getModel())
			.year(carEntity.getYear())
			.kmDriven(carEntity.getKmDriven())
			.description(carEntity.getDescription())
			.color(carEntity.getColor())
			.salePrice(carEntity.getSalePrice())
			.auctionStatus(carEntity.getAuctionStatus())
			.highestBidPrice(carEntity.getHighestBidPrice())
			.currentAuctionStatus(carEntity.getCurrentAuctionStatus())
			.currentAuctionId(carEntity.getCurrentAuctionId())
			.build();
	}
}
