package com.csis3275.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csis3275.models.UserModel;
import com.csis3275.repositories.I_UserRepository;

@Service
public class UserImpl {
	
	@Autowired
	I_UserRepository repository;
	
	// Add User
    public UserModel addUser(UserModel newUser) {
    	newUser.setPassword(PasswordUtils.encryptPassword(newUser.getPassword()));
        System.out.println("User is being added to the database");
        return repository.save(newUser);
    }
    
    // Get User By Email
    public UserModel getUserByEmail(String email) {
    	return repository.findByEmail(email);
    	
    }
    
    // Authorize
    public boolean authorizeUser(String email, String password) {
    	UserModel user = repository.findByEmail(email);
    	if(user != null && PasswordUtils.verifyPassword(password, user.getPassword())){
    		return true; // User is authorized
    	}
    	return false; // User is not authorized
    }
    
    // Delete User
    public boolean deleteUser(String email) {
    	UserModel user = repository.findByEmail(email);
    	if (user != null) {
            try {
                repository.delete(user);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    
    // Change Password
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        UserModel user = repository.findByEmail(email);
        if (user != null && PasswordUtils.verifyPassword(currentPassword, user.getPassword())) {
            user.setPassword(PasswordUtils.encryptPassword( newPassword)); // Update the password
            repository.save(user); // Same ID => Updates User
            return true; 
        } else {
            return false;
        }
    }	    
}