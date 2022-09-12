package com.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.blog.entities.User;
import com.blog.exceptions.UserNotFoundException;
import com.blog.repositories.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
		// Load user from database by username
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User" , " with Email not Found", email));
		
		return user;
	}

}
