package com.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.blog.constants.AppConstants;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.services.FileService;
import com.blog.services.PostService;


@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId){
		PostDto createPost = postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}
	
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostByUserId(@PathVariable Integer userId){
		List<PostDto> listPostsByUser = postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(listPostsByUser, HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostByCategoryId(@PathVariable Integer categoryId){
		List<PostDto> listPostsByCategory = postService.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(listPostsByCategory, HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<PostResponse> getAllPosts( 
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber, 
			@RequestParam(value = "pageSize", defaultValue= AppConstants.PAGE_SIZE, required= false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "post" + AppConstants.SORT_BY , required= false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required= false) String sortDir
			) {
		PostResponse response = postService.getAllPost(pageNumber,pageSize,sortBy, sortDir);
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto postDto = postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto,HttpStatus.OK);
	}
	
	@PutMapping("/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody @Valid PostDto postDto, @PathVariable Integer postId){
		PostDto updatePost = postService.updatePost(postDto,postId);
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		postService.deletePost(postId);
		return new ResponseEntity<>(new ApiResponse("Post Deleted Successfully",true),HttpStatus.OK);
	}
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keyword") String keyword) {
		List<PostDto> list = postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(list,HttpStatus.OK);
	}
	
	@PostMapping("/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadImage( @RequestParam("image") MultipartFile image, @PathVariable Integer postId ) throws IOException{
		
		PostDto postDto = postService.getPostById(postId);
		
		String uploadImage = fileService.uploadImage(path, image);
		
		postDto.setImageName(uploadImage);
		
		PostDto updatePost = postService.updatePost(postDto, postId);
		
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}
	
	@GetMapping(value = "/image/download/imageName", produces = MediaType.IMAGE_JPEG_VALUE )
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
			InputStream inputStream = fileService.getResource(path, imageName);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(inputStream, response.getOutputStream());
	}
	
}
