package com.blog.payloads;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

	
	private int id;
	
	@NotEmpty
	@Size(min = 4, message = "Username must be min of 4 character")
	private String name;
		
	@Email(message = "Email address is not valid")
	//@Pattern(regexp = "\\w+@\\.\\+")
	private String email;
	
	@NotEmpty
	@Size(min = 3, max=10, message="Password must be min 3 and max 10 chars")
	//@Pattern(regexp = "\\w*\\s+\\w*")
	private String password;
	
	@NotNull
	private String about;
}
