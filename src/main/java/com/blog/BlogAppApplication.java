package com.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.blog.constants.AppConstants;
import com.blog.entities.Role;
import com.blog.repositories.RoleRepo;

@SpringBootApplication
@EnableWebMvc
public class BlogAppApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
		System.out.println("Blog-app");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Encoded password : " + passwordEncoder.encode("toxic@1"));
		
		try {
			Role role = new Role();
			role.setRoleId(AppConstants.ADMIN_USER);
			role.setRoleName("ADMIN_USER");

			Role role1 = new Role();
			role1.setRoleId(AppConstants.ADMIN_USER);
			role1.setRoleName("ADMIN_USER");
			
			List<Role> roles = List.of(role, role1);
			
			List<Role> all = roleRepo.saveAll(roles);
			all.forEach(r -> {
				System.out.println(r.getRoleName());
			});
		}catch(Exception e) {
			
		}
	}
	
	/*
	 * Swagger URl: http://localhost:1234/swagger-ui/index.html
	 */
}
