package com.car.auctionms.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor 
@Getter @Setter
@ToString
@Builder
public class CarIdEntity implements Serializable {

	private static final long serialVersionUID = 8066370609614649471L;

	private UserEntity seller;
	private Integer id;
	
	public CarIdEntity(UserEntity seller, Integer id) {
		this.seller = seller;
		this.id = id;
	}
	
}
