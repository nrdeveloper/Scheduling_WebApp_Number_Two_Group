package com.csis3275.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csis3275.models.UserModel;
import com.csis3275.services.SessionManager;
import com.csis3275.services.UserImpl;

import jakarta.servlet.http.HttpSession;


@Controller
public class Auth_controller {

	@Autowired
	UserImpl userService;
	
	// Root: Check if the user is in a session
	@GetMapping("/")
    public String home(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        UserModel user = (UserModel) session.getAttribute("user");
        if (sessionId != null) {
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
	    if (sessionId != null) {
	        SessionManager.removeSession(sessionId);
	        System.out.println("Session Ended successfully. User " + user.getName() + " had been logged out");
	    }
        return "redirect:/login";
    }
}
