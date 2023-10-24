package com.csis3275.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csis3275.models.UserModel;
import com.csis3275.repositories.I_UserRepository;

@Service
public class UserImpl {
	
	@Autowired
	I_UserRepository repository;
	
		// Create
	    public UserModel addUser(UserModel newUser) {
	        System.out.println("User is being added to the database");
	        return repository.save(newUser);
	}
	    
}
