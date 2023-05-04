package com.ankhang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Authen_CodeController {
	@GetMapping("/home")
	public String admin() {
		return "home";
	}
	
	@GetMapping("/homeauthen")
	public String admin2() {
		return "home";
	}
	
}