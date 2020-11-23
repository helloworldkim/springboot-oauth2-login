package com.spring.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.spring.security1.config.oauth.PrincipalOauth2UserService;

//1,구글에서 코드받기(인증) 2. 코드로 엑세스토큰 교환(정보접근 권한)
//3. 사용자 프로필 가져오기 4.1 해당 정보로 회원가입을 진행시킴
//4.2 (이메일,전화번호,이름,아이디) -> 추가적인 정보가 필요하면 따로 정보를 받아야함

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록된다
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private PrincipalOauth2UserService principalDetailsService;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") 	//login 주소가 호출되면 스프링 security가 낚아채서 로그인을 진행해줌
			.defaultSuccessUrl("/") 	//성공시 기본주소를 메인페이지로 설정
			.and()
			.oauth2Login()
			.loginPage("/loginForm") //google로그인 완료될시 (코드x 엑세스토큰O, 사용자 프로필정보O)
			.userInfoEndpoint()
			.userService(principalDetailsService);
		
	}

}
