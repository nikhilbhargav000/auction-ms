package com.car.auctionms.model.request;

import javax.validation.constraints.NotBlank;

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
public class Login {
	
	@NotBlank(message="User email must not be blank")
	private String userEmail;
	@NotBlank(message="User password must not be blank")
	private String password;
	
}
