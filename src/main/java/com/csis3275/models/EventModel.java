package com.csis3275.models;

import java.time.LocalDateTime;




public class EventModel {

	private int id = 1;

	private String title; 

	private String start; 

	private String finish;
	
	public EventModel(String title,String start) {
		setId(id);
		this.title = title;
		this.start = start;
	}
	
	public EventModel(String title,String start, String finish) {
		setId(id);
		this.title = title;
		this.start = start;
		this.finish = finish;
	}
	
	public EventModel() {
		this.id=1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.id +=1;
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
