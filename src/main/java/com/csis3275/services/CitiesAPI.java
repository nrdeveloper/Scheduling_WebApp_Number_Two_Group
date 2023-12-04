package com.csis3275.services;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CitiesAPI {
	private String key = "n0qjFXzKbXeBI7wErk4oXw==Gx7BYROWb6R9QiIr";
	
	public JsonNode fetchCities(String input) throws IOException {
		String encodedInput = URLEncoder.encode(input, "UTF-8");
		URL url = new URL("https://api.api-ninjas.com/v1/city?name=" + encodedInput);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("accept", "application/json");

		connection.setRequestProperty("X-Api-Key", key);

		InputStream responseStream = connection.getInputStream();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(responseStream);
		connection.disconnect();
		return root;
	}
}
