package com.scm;

import com.scm.entities.Chat;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repsitories.ChatRepository;
import com.scm.repsitories.ContactRepo;
import com.scm.repsitories.UserRepo;
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
	private UserRepo userRepo;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private ContactRepo contactRepo;

    @Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setName("Admin");
//		user.setPassword("atul@2001");
		user.setEmail("admin@gmail.com");
		user.setPhoneNumber("1234567890");
		user.setPhoneVerified(true);
		user.setRole(AppConstants.ROLE_ADMIN);
 		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setPassword(this.passwordEncoder.encode("admin@123"));
		user.setAbout("Admin");
		user.setProfilePic("http://res.cloudinary.com/dnhniwrqh/image/upload/c_fill,h_500,w_500/9cfcf9d1-0438-4d81-988b-b49590dcc249");
		boolean userExistByEmail = this.userService.isUserExistByEmail("admin@gmail.com");
		if (userExistByEmail){
			System.out.println("Admin Already register");
		}
		else {
			this.userRepo.save(user);
		}

		/*for (Chat chat : this.chatRepository.findAll()) {
			System.out.println(chat.getMessage()+" ==> "+chat.getSender().getName()+"==> "+chat.getReceiver().getName()+"==> "+chat.getSender().getUserId()+"==> "+chat.getReceiver().getUserId());
		}
*/
//
//		this.userRepo.deleteAll();
//		this.chatRepository.deleteAll();
//		this.chatRepository.deleteAll();
	}
}

