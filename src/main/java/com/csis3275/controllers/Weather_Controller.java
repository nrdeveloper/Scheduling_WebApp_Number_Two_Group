package com.csis3275.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.csis3275.models.UserModel;
import com.csis3275.services.WeatherAPI;
import com.csis3275.services.UserImpl;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpSession;

@Controller
public class Weather_Controller {
	@Autowired
	UserImpl userService;
	@Autowired
	WeatherAPI weather;
	
	// Fetches weather for the next 16 days
	@GetMapping("/fetchWeather")
	public ResponseEntity<?> fetchWeather(HttpSession session) {
        try {
        	UserModel user = (UserModel) session.getAttribute("user");
        	String latitude = user.getLatitude();
        	String longitude = user.getLongitude();
        	if((latitude != null || latitude != "") && (longitude != null || longitude != "")) {
        		JsonNode weatherData = weather.fetchWeather(latitude, longitude);
        		
        		JsonNode hourlyArray = weatherData.path("hourly");
        		
        		JsonNode timeArray = hourlyArray.path("time");
        		JsonNode temperatureArray = hourlyArray.path("temperature_2m");
        		JsonNode rainArray = hourlyArray.path("rain");
        		JsonNode snowArray = hourlyArray.path("snowfall");
        		
        		// Sorting data by hour
        		Map<String, List<Double>> hourlyMap = new HashMap<>();
        		for (int i = 0; i < timeArray.size(); i++) {
        			
        		    String time = timeArray.get(i).asText();
        		    double temperature = temperatureArray.get(i).asDouble();
        		    double rain = rainArray.get(i).asDouble();
        		    double snowfall = snowArray.get(i).asDouble();

        		    if (!hourlyMap.containsKey(time)) {
        		    	hourlyMap.put(time, new ArrayList<>());
                    }

        		    hourlyMap.get(time).add(temperature);
        		    hourlyMap.get(time).add(rain);
        		    hourlyMap.get(time).add(snowfall);
        		}
        		
        		// Sorting data by day
        		Map<String, Map<String, Object>> dailyMap = new HashMap<>();
        		
        		for (Map.Entry<String, List<Double>> entry : hourlyMap.entrySet()) {
        		    String time = entry.getKey();
        		    String day = time.substring(0, 10); // get the date

        		    dailyMap.computeIfAbsent(day, k -> new HashMap<>());
        		    Map<String, Object> dailyValues = dailyMap.get(day);

        		    double temperature = entry.getValue().get(0);
        		    double rain = entry.getValue().get(1);
        		    double snowfall = entry.getValue().get(2);

        		    
        		    dailyValues.compute("minTemp", (key, value) -> (value == null) ? temperature : Math.min((double) value, temperature));
        		    dailyValues.compute("maxTemp", (key, value) -> (value == null) ? temperature : Math.max((double) value, temperature));
        		    
        		    dailyValues.compute("rain", (key, value) -> (value == null) ? rain > 0 : (boolean) value || rain > 0);
        		    dailyValues.compute("snow", (key, value) -> (value == null) ? snowfall > 0 : (boolean) value || snowfall > 0);
        		}

        		dailyMap.forEach((day, values) -> {System.out.println("Day: " + day + ", Min Temp: " + values.get("minTemp") +", Max Temp: " + values.get("maxTemp") +
        		            ", Rain: " + values.get("rain") +", Snow: " + values.get("snow"));
        		});
                return ResponseEntity.ok(weatherData);
        	} else {
        		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No location is set for weather fetching");
        	}
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching weather data");
        }
	}	
}