package com.ankhang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Authen_CodeController {
	@GetMapping("/home")
	public String admin() {
		System.out.println("--------- CALLING /HOME ---------");
		return "home";
	}
	
	@GetMapping("/homeauthen")
	public String admin2() {
		System.out.println("--------- CALLING /HOMEAUTHEN ---------");
		return "home";
	}
	
}