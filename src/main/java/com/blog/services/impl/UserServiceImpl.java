package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.UserDto;
import com.blog.payloads.UserResponse;
import com.blog.repositories.UserRepo;
import com.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		
		User user = this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User"," Id ",userId));	
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		
		User updatedUser = userRepo.save(user);
		
		return this.userToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		return this.userToDto(user);
	}

	@Override
	public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort = Sort.by(sortBy).ascending();
		
		if(sortDir.equalsIgnoreCase("desc")) {
			sort = Sort.by(sortBy).descending();
		}
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<User> userPage = userRepo.findAll(p);
		
		List<User> allUsers = userPage.getContent();
		
		//List<User> users = userRepo.findAll();
		List<UserDto> userDtos = allUsers.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		
		UserResponse userResponse = new UserResponse();
		
		userResponse.setLastPage(userPage.isLast());
		userResponse.setListUserDto(userDtos);
		userResponse.setPageNumber(userPage.getNumber());
		userResponse.setPageSize(userPage.getSize());
		userResponse.setTotalElements(userPage.getTotalElements());
		userResponse.setTotalPages(userPage.getTotalPages());
		
		return userResponse;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		userRepo.delete(user);

	}
	
	private User dtoToUser(UserDto userDto) {
		
		User user = modelMapper.map(userDto, User.class);
		/*
		 * user.setId(userDto.getId()); user.setName(userDto.getName());
		 * user.setEmail(userDto.getEmail()); user.setPassword(userDto.getPassword());
		 * user.setAbout(userDto.getAbout());
		 */
		
		return user;
	}
	
	public UserDto userToDto(User user) {
		UserDto userDto = modelMapper.map(user, UserDto.class); // new UserDto();
		
		/*
		 * userDto.setId(user.getId()); userDto.setName(user.getName());
		 * userDto.setEmail(user.getEmail()); userDto.setPassword(user.getPassword());
		 * userDto.setAbout(user.getAbout());
		 */
		
		return userDto;
	}
	
}
