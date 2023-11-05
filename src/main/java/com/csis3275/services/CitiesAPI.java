package com.csis3275.services;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class CitiesAPI {
	private String key = "n0qjFXzKbXeBI7wErk4oXw==Gx7BYROWb6R9QiIr";
	
	public String fetchCities(String input) throws IOException {
		URL url = new URL("https://api.api-ninjas.com/v1/city?name=" + input);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("accept", "application/json");

		// Set your API key as an "X-Api-Key" header
		connection.setRequestProperty("X-Api-Key", key); // Replace with your actual API key

		InputStream responseStream = connection.getInputStream();
		try (Scanner scanner = new Scanner(responseStream, StandardCharsets.UTF_8).useDelimiter("\\A")) {
			String response = scanner.hasNext() ? scanner.next() : "";
			return (response);
		}
	}
}
