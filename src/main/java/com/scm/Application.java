package com.scm;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private UserService userService;

    @Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setName("Atul Patel");
		user.setPassword("7096773572");
		user.setPhoneVerified(true);
		user.setRole(AppConstants.ROLE_ADMIN);
		user.setEmail("admin2001@gmail.com");
		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setPassword(this.passwordEncoder.encode("atul@2001"));
		boolean userExistByEmail = this.userService.isUserExistByEmail("admin2001@gmail.com");
		if (userExistByEmail){
			System.out.println("Admin Already register");

		}
		else {
			this.userService.saveUser(user);
		}
	}
}
