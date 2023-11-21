package com.csis3275.models;

import java.time.LocalDateTime;




public class EventModel {


	private String title; 

	private String start; 

	private String finish;
	
	public EventModel(String title,String start) {
		this.title = title;
		this.start = start;
	}
	
	public EventModel(String title,String start, String finish) {
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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	@Override
	public String toString() {
		return "Event [title=" + title + ", start=" + start
				+ ", finish=" + finish + "]";
	} 	
}
