package com.st.emailverificationusecase.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/home")
@RestController
public class HomeController {

	@RequestMapping("/isalive")
	public boolean isAlive(){
		return true;
	}
}
