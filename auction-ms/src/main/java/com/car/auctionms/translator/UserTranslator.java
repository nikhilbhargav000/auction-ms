package com.car.auctionms.translator;

import org.springframework.stereotype.Component;

import com.car.auctionms.entity.UserEntity;
import com.car.auctionms.model.car.User;

@Component
public class UserTranslator {

	public User getUser(UserEntity userEntity) {
		return User.builder()
				.userId(userEntity.getUserId())
				.password(userEntity.getPassword())
				.email(userEntity.getEmail())
				.name(userEntity.getName())
				.role(userEntity.getRole())
				.build();
	}
	
	public UserEntity getUser(User user) {
		return UserEntity.builder()
				.userId(user.getUserId())
				.password(user.getPassword())
				.email(user.getEmail())
				.name(user.getName())
				.role(user.getRole())
				.build();
	}
}
