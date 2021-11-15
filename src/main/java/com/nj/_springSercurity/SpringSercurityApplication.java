package com.nj._springSercurity;

import com.nj._springSercurity.domain.Role;
import com.nj._springSercurity.domain.User;
import com.nj._springSercurity.dto.AddRoleToUser;
import com.nj._springSercurity.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringSercurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSercurityApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	/*@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_MANAGER"));

			userService.saveUser(new User(null,"Naveen J" ,"naveen","1234",new ArrayList()));
			userService.saveUser(new User(null,"Sandip J" ,"sandip","1234",new ArrayList()));
			userService.saveUser(new User(null,"Mamatha R" ,"mamatha","1234",new ArrayList()));

			userService.addRoleToUser("naveen","ROLE_USER");
			userService.addRoleToUser("naveen","ROLE_ADMIN");
			userService.addRoleToUser("mamatha","ROLE_USER");
			userService.addRoleToUser("sandip","ROLE_MANAGER");
		};

	}
*/
}
