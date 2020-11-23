package com.spring.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.security1.config.auth.PrincipalDetails;
import com.spring.security1.model.User;
import com.spring.security1.repository.UserRepository;

@Controller
public class indexController {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping({"/",""})
	public String index() {
		
		//머스테치 기본폴더 src/main/resources/ 
		//뷰리졸버 설정 : templates(prefix), .mustache(suffix) (생략가능함!)
		return "index";	// src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/test/login")
	@ResponseBody
	public String testLogin(Authentication authentication,
//			@AuthenticationPrincipal UserDetails userDetails) { 
		@AuthenticationPrincipal PrincipalDetails userDetails) {
	 System.out.println("/test/login=========================");
	 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
	 System.out.println("authentication:"+principalDetails);
	 
	 System.out.println("userDetails:"+userDetails.getUser());
	 return "세션정보확인";
	}
	@GetMapping("/test/oauth/login")
	@ResponseBody
	public String testLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) {
	 System.out.println("/test/oauthlogin=========================");
	 OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
	 System.out.println("authentication:"+oauth2User.getAttributes());
	 System.out.println("OAuth2User:"+oauth.getAttributes());
	 
	 return "oauth 세션정보확인";
	}
	
	@GetMapping("/user")
	@ResponseBody
	public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails:"+principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPass = user.getPassword();
		String encPass = passwordEncoder.encode(rawPass);
		user.setPassword(encPass);
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	
}
