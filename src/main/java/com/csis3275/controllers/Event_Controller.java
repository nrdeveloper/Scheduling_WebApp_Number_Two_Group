package com.csis3275.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Event_Controller {

		@GetMapping("/events")
		public String events() {
			return "calendar";
		}
	}

