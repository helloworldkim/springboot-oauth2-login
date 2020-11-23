package com.spring.security1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.spring.security1.config.auth.PrincipalDetails;
import com.spring.security1.config.oauth.provider.FacebookUserInfo;
import com.spring.security1.config.oauth.provider.GoogleUserInfo;
import com.spring.security1.config.oauth.provider.NaverUserInfo;
import com.spring.security1.config.oauth.provider.OAuth2UserInfo;
import com.spring.security1.model.User;
import com.spring.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;

	//구글로 부터 받는 userRequest 데이터에 대한 후처리함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration:"+userRequest.getClientRegistration());	//registrationId로 어떤 OAuth로 로그인했는지 확인가능
		System.out.println("getAccessToken:"+userRequest.getAccessToken().getTokenValue());
		
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		//구글 로그인버튼 클릭-> 구글 로그인창 -> 로그인완료->code받음(OAuth2-client라이브러리-> AccessToken 요청
		//회원정보요청 -> loadUser함수->회원프로필 받음
		System.out.println("getAttribute:"+oAuth2User.getAttributes());
		
		
		//회원가입을 강제로 할 예정
		OAuth2UserInfo oAuth2UserInfo =null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인요청");
			oAuth2UserInfo=new GoogleUserInfo(oAuth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인요청");
			oAuth2UserInfo=new FacebookUserInfo(oAuth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인요청");
			//naver는 데이터를 response라는 키값에 담에서 return 해줌
			oAuth2UserInfo=new NaverUserInfo((Map<String, Object>)oAuth2User.getAttributes().get("response"));
		}else {
			System.out.println("우리는 구글 페이스북 네이버만 지원합니다");
		}
		
//		String provider =userRequest.getClientRegistration().getRegistrationId(); 		//google
		String provider =		oAuth2UserInfo.getProvider();
//		String providerId = oAuth2User.getAttribute("sub");
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+providerId;	//google_고유한 primary key값
		String password = passwordEncoder.encode("헬로우");	//임의의값을 암호화해서 자동가입
//		String email = oAuth2User.getAttribute("email");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		//가입시키는부분 일단 해당 아이디가 있는지 확인
		User userEntity = userRepository.findByUsername(username);
		if(userEntity ==null) {
			System.out.println("OAuth 로그인이 최초입니다");
			userEntity= User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		System.out.println("로그인을 이미 한적이 있습니다 자동으로 로그인됩니다");
		
		return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
	}
}
