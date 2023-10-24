package com.csis3275.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csis3275.models.UserModel;
import com.csis3275.services.UserImpl;


@Controller
public class Auth_controller {

	@Autowired
	UserImpl userService;
	
	// Login
	@GetMapping("/login")
	public String loginPage() {
		return ("login");
	}
	
	// Authorize
	@PostMapping("/login")
	public String auth(@RequestParam String email, @RequestParam String password) {
		boolean authorized = userService.authorizeUser(email, password);
		if (authorized) {
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
}
