package com.csis3275.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csis3275.models.UserModel;
import com.csis3275.services.CitiesAPI;
import com.csis3275.services.UserImpl;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpSession;

@Controller
public class Cities_Controller {
	@Autowired
	UserImpl userService;
	@Autowired
	CitiesAPI citiesAPI;
	
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
