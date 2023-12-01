package com.csis3275.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeatherAPI {
	public JsonNode fetchWeather(String latitude, String longitude) throws IOException {
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +"&longitude=" +  longitude +"&current=precipitation,rain,showers&hourly=temperature_2m,rain,snowfall&timezone=auto&forecast_days=16";
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

    		InputStream responseStream = connection.getInputStream();
    		ObjectMapper mapper = new ObjectMapper();
    		JsonNode root = mapper.readTree(responseStream);
    		
    		
    		
    		connection.disconnect();
    		return root;
        }
}

