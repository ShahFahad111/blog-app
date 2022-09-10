package com.blog.services.impl;

import java.util.Date;
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
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;

@Service
public class PostServiceImpl  implements PostService{

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public PostDto createPost(PostDto postDto,  Integer userId, Integer categoryId) {
		
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", categoryId));
		
		Post post = modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post savedPost = postRepo.save(post);
		
		return modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto,Integer postId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", " Id ", postId));
		
		//post.setAddedDate(new Date());
		post.setContent(postDto.getContent());
		post.setTitle(postDto.getTitle());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = postRepo.save(post);
		return modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", " Id ", postId));
		postRepo.delete(post);
		
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort = Sort.by(sortBy).ascending();
		
		if(sortDir.equalsIgnoreCase("desc"))
			sort = Sort.by(sortBy).descending();
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Post> postPage = postRepo.findAll(p);
		List<Post> allPosts = postPage.getContent();
		List<PostDto> list = allPosts.stream().map(i -> modelMapper.map(i, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse  = new PostResponse();
		postResponse.setListPostDto(list);
		postResponse.setPageNumber(postPage.getNumber());
		postResponse.setPageSize(postPage.getSize());
		postResponse.setTotalElements(postPage.getTotalElements());
		postResponse.setTotalPages(postPage.getTotalPages());
		postResponse.setLastPage(postPage.isLast());
		
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", " Id ", postId));
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", categoryId));
		
		List<Post> findByCategory = postRepo.findByCategory(category);
		
		List<PostDto> list = findByCategory.stream().map(i -> modelMapper.map(i, PostDto.class)).collect(Collectors.toList());
		
		return list;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		
		List<Post> findAllByUser = postRepo.findAllByUser(user);
		
		List<PostDto> l = findAllByUser.stream().map(i -> modelMapper.map(i,PostDto.class)).collect(Collectors.toList());
		return l;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}
