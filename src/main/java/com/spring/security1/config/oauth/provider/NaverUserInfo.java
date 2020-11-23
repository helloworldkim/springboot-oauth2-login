package com.spring.security1.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

	private Map<String,Object> attributes; //Oauth2User.getAttributes() 를 받을예정
	
	public NaverUserInfo(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	@Override
	public String getProviderId() {
		return (String) attributes.get("id"); //Facebook 정보 확인시 아이디값
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

}
