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
import com.blog.payloads.CategoryDto;
import com.blog.payloads.CategoryResponse;
import com.blog.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto){
		CategoryDto createdCategory = categoryService.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(createdCategory, HttpStatus.CREATED);
	}
	
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto, @PathVariable Integer categoryId){
		CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updatedCategory,HttpStatus.OK);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer categoryId){
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(new ApiResponse("Category Deleted Successfully",true),HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable Integer categoryId){
		CategoryDto categoryDto = categoryService.getCategory(categoryId);
		return new ResponseEntity<CategoryDto>(categoryDto,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<CategoryResponse> getAllCategory(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "categoryId", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		CategoryResponse categoryResponse = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<CategoryResponse>(categoryResponse,HttpStatus.OK);
	}
}
