package com.spring.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.security1.model.User;
import com.spring.security1.repository.UserRepository;

//시큐리티 설정에서 loginProcessingUrl("/login");요청이오면
//UserDetailsService의 loadUserByUserName함수가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	//security session  = Authentication = UserDetails
	//securitySession(Authentication(UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity =  userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

}
