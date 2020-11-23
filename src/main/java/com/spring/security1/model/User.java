package com.spring.security1.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private int id;
	private String username;
	private String password;
	private String email;
	private String role; //ROLE_USER ROLE_MANAGER ROLE_ADMIN
	
	//2개 추가함 oauth2로 로그인처리할때 구별하기위한부분
	private String provider; 				//google
	private String providerId;			// google의 primary key 부분 ex) sub부분
	@CreationTimestamp
	private Timestamp createDate;
	
	@Builder
	public User(String username, String password, String email, String role, String provider, String providerId,
			Timestamp createDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.createDate = createDate;
	}
	
	
}
