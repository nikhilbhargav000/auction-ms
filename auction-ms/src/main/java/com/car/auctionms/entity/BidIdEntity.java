package com.car.auctionms.entity;

import java.io.Serializable;

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
public class BidIdEntity implements Serializable {

	private static final long serialVersionUID = -7532854547535553947L;
	
	private Integer carId;
	private Integer id;
	
}
