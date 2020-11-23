package com.spring.security1.config.oauth.provider;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

	private Map<String,Object> attributes; //Oauth2User.getAttributes() 를 받을예정
	
	public GoogleUserInfo(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	@Override
	public String getProviderId() {
		return (String) attributes.get("sub"); //google 정보 확인시 아이디값
	}

	@Override
	public String getProvider() {
		return "google";
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
