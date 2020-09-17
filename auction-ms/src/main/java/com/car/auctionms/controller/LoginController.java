package com.car.auctionms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.car.auctionms.exception.CarAppsException;
import com.car.auctionms.model.request.Login;
import com.car.auctionms.model.response.LoginResponse;
import com.car.auctionms.service.UserService;

@RestController
public class LoginController {
	
	private UserService userService;
	
	@Autowired
	public LoginController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public LoginResponse login(@RequestBody @Valid Login login, BindingResult result) {
		if (result.hasErrors())
			throw new CarAppsException(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().get(0).toString());
		
		LoginResponse response = userService.login(login);
		return response;
	}
}
