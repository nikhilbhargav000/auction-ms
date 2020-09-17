package com.car.auctionms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.car.auctionms.model.response.CarAppsResponseMessage;

@ControllerAdvice
public class CarAppsExceptionHandler {

	Logger logger = LoggerFactory.getLogger(CarAppsExceptionHandler.class);
	
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<CarAppsResponseMessage> exceptionHandler(Exception e) {
		
		CarAppsResponseMessage response = new CarAppsResponseMessage(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getMessage());
		logger.error("Exception occurred", e);
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	@ExceptionHandler(CarAppsException.class)
	public ResponseEntity<CarAppsResponseMessage> sAppExceptionHandler(CarAppsException e) {
		
		CarAppsResponseMessage response = new CarAppsResponseMessage(e.getResponseCode(), e.getMessage());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
