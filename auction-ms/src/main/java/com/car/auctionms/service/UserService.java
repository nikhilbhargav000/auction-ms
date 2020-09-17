package com.car.auctionms.service;

import com.car.auctionms.model.request.Login;
import com.car.auctionms.model.response.LoginResponse;

public interface UserService {

	public LoginResponse login(Login login) ;
	
}
