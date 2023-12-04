package com.csis3275.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.csis3275.models.EventModel;
import com.csis3275.models.UserModel;
import com.csis3275.repositories.I_UserRepository;

import jakarta.servlet.http.HttpSession;

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
    
    //Update City Parameters
    public void updateCity(String email, String city, String latitude, String longitude) {
    	UserModel user = repository.findByEmail(email);
    	user.setCity(city);
    	user.setLatitude(latitude);
    	user.setLongitude(longitude);
    	repository.save(user);
    }
    
    public void addEventToUser(String email, EventModel newEvent) {
        UserModel user = repository.findByEmail(email);

        if (user != null) {
            user.setEvents((EventModel) newEvent);
            repository.save(user);
        }
    }
    
    public void deleteEventToUser(String email, int id) {
    	 UserModel user = repository.findByEmail(email);
    	 List<EventModel> events = user.getEvents();
    	 for (EventModel eventModel : events) {
			if(id == eventModel.getId()){
				events.remove(id);
			}
		}
    	 user.setEvents((EventModel) events);

         repository.save(user);
    }
}
