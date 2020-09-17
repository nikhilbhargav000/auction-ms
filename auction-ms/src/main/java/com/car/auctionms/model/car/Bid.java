package com.car.auctionms.model.car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

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
public class Bid {

	@Null(message="Bid id must be null")
	private Integer id;
	
	@NotBlank(message="Bid price can not be blank")
	private Integer price;

	@Null(message="Car id must be null")
	private Integer carId;
	@Null(message="User id must be null")
	private String userId;

}
