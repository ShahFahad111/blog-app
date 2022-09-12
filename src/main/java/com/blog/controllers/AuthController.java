package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;
import com.blog.security.JwtTokenHelper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception{
		this.authenticate(request.getEmail(), request.getPassword());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		
		String token = jwtTokenHelper.generateToken(userDetails);
		
		JwtAuthResponse response = new JwtAuthResponse();
		response.setAuthResponse(token);
		
		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
	}

	private void authenticate(String email, String password) throws Exception {
		
		UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken(email,password);

		try {
			authenticationManager.authenticate(u);
		}catch(BadCredentialsException e) {
			System.out.println("Invalid Password Exception");
			throw new Exception("Invalid Username or Password");
		}

	}
	
}
