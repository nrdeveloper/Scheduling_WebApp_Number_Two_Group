package com.csis3275.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.csis3275.models.UserModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class WeatherAPI {
	public JsonNode fetchWeatherAPI(String latitude, String longitude) throws IOException {
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +"&longitude=" +  longitude +"&current=precipitation,rain,showers&hourly=temperature_2m,rain,snowfall,showers&timezone=auto&forecast_days=16";
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

    		InputStream responseStream = connection.getInputStream();
    		ObjectMapper mapper = new ObjectMapper();
    		JsonNode root = mapper.readTree(responseStream);
    		connection.disconnect();
    		return root;
        }
	
	public List<List<Object>> fetchWeather(HttpSession session) throws IOException {
        	UserModel user = (UserModel) session.getAttribute("user");
        	String latitude = user.getLatitude();
        	String longitude = user.getLongitude();
        	List<List<Object>> resultArray = new ArrayList<>();
        	if((latitude != null || latitude != "") && (longitude != null || longitude != "")) {
        		JsonNode weatherData = fetchWeatherAPI(latitude, longitude);
        		
        		JsonNode hourlyArray = weatherData.path("hourly");
        		
        		JsonNode timeArray = hourlyArray.path("time");
        		JsonNode temperatureArray = hourlyArray.path("temperature_2m");
        		JsonNode rainArray = hourlyArray.path("rain");
        		JsonNode showersArray = hourlyArray.path("showers");
        		JsonNode snowArray = hourlyArray.path("snowfall");
        		
        		// Sorting data by hour
        		Map<String, List<Double>> hourlyMap = new HashMap<>();
        		for (int i = 0; i < timeArray.size(); i++) {
        			
        		    String time = timeArray.get(i).asText();
        		    double temperature = temperatureArray.get(i).asDouble();
        		    double rain = rainArray.get(i).asDouble();
        		    double snowfall = snowArray.get(i).asDouble();
        		    double showers = showersArray.get(i).asDouble();

        		    if (!hourlyMap.containsKey(time)) {
        		    	hourlyMap.put(time, new ArrayList<>());
                    }

        		    hourlyMap.get(time).add(temperature);
        		    hourlyMap.get(time).add(rain);
        		    hourlyMap.get(time).add(snowfall);
        		    hourlyMap.get(time).add(showers);
        		}
        		
        		// Sorting data by day
        		Map<String, Map<String, Object>> dailyMap = new HashMap<>();

        		for (Map.Entry<String, List<Double>> entry : hourlyMap.entrySet()) {
        		    String time = entry.getKey();
        		    String day = time.substring(0, 10); // get the date

        		    dailyMap.computeIfAbsent(day, key -> new HashMap<>());
        		    Map<String, Object> dailyValues = dailyMap.get(day);

        		    double temperature = entry.getValue().get(0);
        		    double rain = entry.getValue().get(1);
        		    double snowfall = entry.getValue().get(2);
        		    double showers = entry.getValue().get(3);
        		    
        		    dailyValues.compute("minTemp", (key, value) -> (value == null) ? temperature : Math.min((double) value, temperature));
        		    dailyValues.compute("maxTemp", (key, value) -> (value == null) ? temperature : Math.max((double) value, temperature));
        		    
        		    
        		    // Determine how many hours per day have one of the weather conditions
        		    dailyValues.compute("totalSnowfall", (key, value) -> (value == null) ? 0 : (int) value + (snowfall > 0 ? 1 : 0));
        		    dailyValues.compute("totalRain", (key, value) -> (value == null) ? 0 : (int) value + (rain > 0 ? 1 : 0));
        		    dailyValues.compute("totalShowers", (key, value) -> (value == null) ? 0 : (int) value + (showers > 0 ? 1 : 0));
        		    
        		    if (rain == 0.0 && snowfall == 0.0 && showers == 0.0) {
        		        dailyValues.compute("clearHours", (key, value) -> (value == null) ? 1 : (int) value + 1);
        		    } else {
        		        dailyValues.compute("clearHours", (key, value) -> (value == null) ? 0 : (int) value);
        		    }
        		    
        		}
        		
        		
        		
        		dailyMap.forEach((day, values) -> {
        			double maxSnowfall = (int) values.getOrDefault("totalSnowfall", 0.0);
        		    double maxRain = (int) values.getOrDefault("totalRain", 0.0);
        		    double maxShowers = (int) values.getOrDefault("totalShowers", 0.0);
        		    double clearHours = (int) values.getOrDefault("clearHours", 0.0);
        		    
        		    String dominantWeather = "Clear";
        		    double maxWeatherValue = 0.0;
        		    
        		    if (maxSnowfall > maxWeatherValue) {
        		        dominantWeather = "Snowfall";
        		        maxWeatherValue = maxSnowfall;
        		    }
        		    if (maxRain > maxWeatherValue) {
        		        dominantWeather = "Rain";
        		        maxWeatherValue = maxRain;
        		    }
        		    if (maxShowers > maxWeatherValue) {
        		        dominantWeather = "Showers";
        		        maxWeatherValue = maxShowers;
        		    }
        		    if (clearHours > maxWeatherValue) {
        		        dominantWeather = "Clear";
        		        maxWeatherValue = clearHours;
        		    }
        		    
        		    List<Object> row = Arrays.asList(
        		            day,
        		            values.get("minTemp"),
        		            values.get("maxTemp"),
        		            dominantWeather
        		    );
        		    resultArray.add(row);
        		    
        		});
        		
        		// Sorting by date
        		Collections.sort(resultArray, Comparator.comparing(row -> (String) row.get(0)));
                return resultArray;
        	}
			return resultArray;
}}

