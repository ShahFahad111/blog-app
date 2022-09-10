package com.blog.services;

import com.blog.payloads.UserDto;
import com.blog.payloads.UserResponse;

public interface UserService {

	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	UserDto getUserById(Integer userId);
	
	UserResponse getAllUsers(Integer pageNumber, Integer pageSize);
	
	void deleteUser(Integer userId);
	
}
