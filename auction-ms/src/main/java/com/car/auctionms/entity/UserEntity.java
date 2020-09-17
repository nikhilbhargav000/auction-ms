package com.car.auctionms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="users")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
public class UserEntity {
	
//	private static final long serialVersionUID = 471918624109760598L;
	
	@Id
	@Column(name="user_id")
	private String userId;
	private String name;
	private String email;
	private String password;
	@Column(name="role")
	private Role role;
	
	
	/**
	 * User Roles
	 */
	public static enum Role {
		USER, 
		ADMIN;
	}
}
