package com.blog.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {

	String resourceName;
	
	String fieldName;

	String filedValueString;
	
	
	public UserNotFoundException(String resourceName, String fieldName, String filedValueString) {
		super(String.format("%s not found with %s : %s",resourceName, fieldName,filedValueString));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.filedValueString = filedValueString;
	}
}
