package com.blog.services;

import com.blog.payloads.UserDto;
import com.blog.payloads.UserResponse;

public interface UserService {
	
	UserDto registerNewUser(UserDto userDto);

	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	UserDto getUserById(Integer userId);
	
	UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	void deleteUser(Integer userId);
	
}
