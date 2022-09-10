package com.blog.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
	
	private Integer categoryId;
	
	@NotBlank
	@Size(min = 4, message = "Min Category Title length is 4")
	private String categoryTitle;
	
	@NotBlank
	@Size(min = 5, message = "Min Category Description length is 5")
	private String categoryDescription;
}
