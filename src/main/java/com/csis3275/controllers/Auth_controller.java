package com.csis3275.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csis3275.models.UserModel;
import com.csis3275.services.SessionManager;
import com.csis3275.services.UserImpl;
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
    public String home(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        UserModel user = (UserModel) session.getAttribute("user");
        if (sessionId != null && user != null) {
            // User is in a session
        	System.out.println("User -" + user.getName() +"- is in an active session");
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
            System.out.println("Login successful");
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
		return ("redirect:/");
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
	
	@GetMapping("/city")
    public String searchCity(/*@RequestParam String cityName*/) {
        try {
            String response = citiesAPI.fetchCities("");
            System.out.println(response);
            return ("redirect:/");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return "Error occurred: " + e.getMessage();
        }
    }
	
}
