package com.blog.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.UserDto;
import com.blog.payloads.UserResponse;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto createdUserDto = userService.createUser(userDto);
		return new ResponseEntity<>(createdUserDto,HttpStatus.CREATED);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto, @PathVariable("userId") Integer uid){
		UserDto updatedUserDto = userService.updateUser(userDto, uid);
		return new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
		userService.deleteUser(userId);
		return new ResponseEntity<>(new ApiResponse("User Deleted Successfully",true),HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<UserResponse> getUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			) {
		return ResponseEntity.ok(userService.getAllUsers(pageNumber, pageSize,sortBy,sortDir));
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId) {
		UserDto userDto = userService.getUserById(userId);
		return ResponseEntity.ok(userDto);
	}
}
