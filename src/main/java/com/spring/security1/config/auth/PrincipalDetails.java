package com.spring.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.spring.security1.model.User;

import lombok.Data;

//Authentication 객체로 security session에 저장해줌
// security sessio => Authentication => userDetails
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user; //콤포지션
	private Map<String,Object> attributes;
	
	//일반로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//oauth 로그인!
	public PrincipalDetails(User user,Map<String,Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	//해당 유저의 권한을 return 하는곳!
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// user.getRole() String 타입으로 바로 리턴은 안된다!
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
				
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	//만료
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	//잠금
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	//비밀번호 유효기간 여부
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		//1년동안 로그인 안할때 휴면계정으로 바꾸고싶다면
		//login날짜를 받아두고 현재시간 - 로그인시간이 1년을 초과할때 return false를 주는형식으로 하면 
		//설정할수있음
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		//아이디값 google치면 sub의값이 primary key임
		//return을 원한다면  attributes.get("sub")
		return null;
	}

}
