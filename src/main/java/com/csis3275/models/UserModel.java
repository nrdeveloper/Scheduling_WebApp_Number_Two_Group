package com.csis3275.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class UserModel {
	
	@Id
	private String id;
	@Field
	private String name;
	@Field
	private String email;
	@Field
	private String password;
	@Field
	private String city;
	@Field 
	private String longitude;
	@Field 
	private String latitude;

	@Field
	private List<List<Object>> weather; // For session only
	
	public UserModel(String id, String name, String email, String password, String city, List<EventModel> events) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.city = city;
		this.events = events;
	}

	@Field
    private List<EventModel> events = new ArrayList();
	
	public List<EventModel> getEvents() {
		return events;
	}

	public void setEvents(EventModel events) {
		this.events.add((EventModel) events);
	}

	// Default Constructor
	public UserModel() {}

	public UserModel(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setWeather(List<List<Object>> weather) {
		this.weather = weather;
	}
	public List<List<Object>> getWeather() {
		return weather;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}
}
