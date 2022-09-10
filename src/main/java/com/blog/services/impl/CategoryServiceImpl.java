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

import com.blog.entities.Category;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CategoryDto;
import com.blog.payloads.CategoryResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = modelMapper.map(categoryDto, Category.class);
		Category savedCategory = categoryRepo.save(category);
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", categoryId));
		
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		
		Category updatedCategory = categoryRepo.save(category);
		
		return modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", categoryId));
		
		categoryRepo.delete(category);
	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", categoryId));
		return modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public CategoryResponse getAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort = Sort.by(sortBy).ascending();
		
		if(sortDir.equalsIgnoreCase("desc"))
			sort = Sort.by(sortBy).descending();
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Category> categoryPage = categoryRepo.findAll(p);
		
		List<Category> allCateogry = categoryPage.getContent();
		
		//List<Category> categoryList = categoryRepo.findAll();
		List<CategoryDto> categoryDtoList = allCateogry.stream().map(i -> modelMapper.map(i, CategoryDto.class)).collect(Collectors.toList());
		
		CategoryResponse categoryResponse = new  CategoryResponse();
		
		categoryResponse.setListCategoryDto(categoryDtoList);
		categoryResponse.setLastPage(categoryPage.isLast());
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		
		return categoryResponse;
	}

}
