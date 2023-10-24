package com.csis3275;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.csis3275.models.UserModel;
import com.csis3275.repositories.I_UserRepository;

@SpringBootApplication
public class SchedulingWebAppNumberTwoGroupApplication implements CommandLineRunner{
	
	@Autowired
	I_UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SchedulingWebAppNumberTwoGroupApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(userRepository.findAll().isEmpty()) {
			userRepository.save(new UserModel("Sam Hill", "samhill@gmail.com", "ioqwhejr"));
		}
		
		for (UserModel user : userRepository.findAll()) {
			System.out.println(user);
		}
		
	}

}
