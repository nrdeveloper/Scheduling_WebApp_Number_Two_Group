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
	
	public EventModel(String title,LocalDateTime start, LocalDateTime finish) {
		this.title = title;
		this.start = start;
		this.finish = finish;
	}
	
	public EventModel() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		return "Event [title=" + title + ", start=" + start
				+ ", finish=" + finish + "]";
	} 	
}
