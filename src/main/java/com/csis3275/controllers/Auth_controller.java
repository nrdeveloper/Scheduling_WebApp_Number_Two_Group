package com.csis3275.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csis3275.models.EventModel;
import com.csis3275.models.UserModel;
import com.csis3275.services.SessionManager;
import com.csis3275.services.UserImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.csis3275.services.CitiesAPI;

import jakarta.servlet.http.HttpSession;


@Controller
public class Auth_controller {

	@Autowired
	UserImpl userService;
	@Autowired
	CitiesAPI citiesAPI;
	
	// Root: Check if the user is in a session
	@GetMapping("/")
    public String home(Model model, HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        UserModel user = (UserModel) session.getAttribute("user");
        if (sessionId != null && user != null) {
            // User is in a session
        	System.out.println("User -" + user.getName() +"- is in an active session");
        	model.addAttribute("userCity", user.getCity());
        	List<EventModel> events = user.getEvents();
        	model.addAttribute("userEvents", events);
        	System.out.println(events);
            return "home";
        } else {
            return "redirect:/login";
        }
    }
	
	// Login
	@GetMapping("/login")
	public String loginPage() {
		return ("login");
	}
	
	// Authorize
	@PostMapping("/login")
	public String auth(@RequestParam String email, @RequestParam String password, HttpSession session) {
		boolean authorized = userService.authorizeUser(email, password);
		if (authorized) {
			// User is Authorized -> Create a session
            UserModel user = userService.getUserByEmail(email);
            String sessionId = SessionManager.createSession(user.getName(), email, password); 
            session.setAttribute("sessionId", sessionId);
            session.setAttribute("user", user);
            session.setAttribute("userEvents", user.getEvents());
            System.out.println(user.getEvents());
            System.out.println("Login successful");
          //  EventModel event = new EventModel("Birthday Party","2023-11-20");
           // List<EventModel> mytest = new 
            //user.setEvents(event);
           // userService.addEventToUser(email, event);
            
            
            return ("redirect:/");
        } else {
        	System.out.println("Login failed");
            return ("redirect:/login");
        }
    }

	// Register
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("addUser", new UserModel());
		return ("register");
	}

	@PostMapping("/register")
	public String registerPage(UserModel addUser) {
		userService.addUser(addUser);
		return ("redirect:/login");
	}
	
	// LogOut -> Delete Session
	@GetMapping("/logout")
    public String logout(HttpSession session) {
		UserModel user = (UserModel) session.getAttribute("user");
		String sessionId = (String) session.getAttribute("sessionId");
		System.out.println("Ending session for session ID: " + sessionId);
	    if (sessionId != null && user != null) {
	    	SessionManager.removeSession(sessionId);
	        System.out.println("User -" + user.getName() + "- had been logged out ");
	    }
        return "redirect:/login";
    }
	
	// Delete Account
	@GetMapping("/delete-current-user")
    public String deleteUser(HttpSession session) {
		UserModel user = (UserModel) session.getAttribute("user");
		String sessionId = (String) session.getAttribute("sessionId");
	    if (sessionId != null && user != null) {
	    	SessionManager.removeSession(sessionId);
	        System.out.println("User has been deleted -" + user.getName() + "-has been deleted from the DataBase");
	        userService.deleteUser(user.getEmail());
	    }
        return "redirect:/login";
    }
	
	// Change Password
	@GetMapping("/change-password")
	public String changePasswordPage() {
		return ("change-password");
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, HttpSession session) {
		UserModel user = (UserModel) session.getAttribute("user");
		boolean passwordChange = false;
		if(user != null) {
			passwordChange = userService.changePassword(user.getEmail(), currentPassword, newPassword);
		}
		if (passwordChange) {
            System.out.println("Password was updated for user -" + user.getName() + "-");
            return ("redirect:/");
        } else {
        	System.out.println("Password update failed");
            return ("redirect:/");
        }
    }
	
	// City
	@GetMapping("/city")
	public ResponseEntity<?> searchCity(@RequestParam String cityName) {
        try {
        	JsonNode cityData = citiesAPI.fetchCities(cityName);
            return ResponseEntity.ok(cityData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching city data");
        }
	}	
	
	@GetMapping("/setCity")
	public String setCity(@RequestParam String city, @RequestParam String latitude, @RequestParam String longitude, HttpSession session) {
		UserModel user = (UserModel) session.getAttribute("user");
		userService.updateCity(user.getEmail(), city, latitude, longitude);
		user.setCity(city);
		user.setLatitude(latitude);
		user.setLongitude(longitude);
		System.out.println("City was updated to " + user.getCity());
		
		return ("redirect:/");
	}
}