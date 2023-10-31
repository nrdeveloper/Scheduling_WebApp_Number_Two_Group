package com.csis3275.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class EventModel {

	@Id 
	private String id;
	@Field
	private String title; 
	@Field
	private String description;
	@Field
	private LocalDateTime start; 
	@Field
	private LocalDateTime finish;
	
	public EventModel(String id, String title, String description, LocalDateTime start, LocalDateTime finish) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.start = start;
		this.finish = finish;
	}
	
	public EventModel() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getFinish() {
		return finish;
	}

	public void setFinish(LocalDateTime finish) {
		this.finish = finish;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + title + ", description=" + description + ", start=" + start
				+ ", finish=" + finish + "]";
	} 	
}
