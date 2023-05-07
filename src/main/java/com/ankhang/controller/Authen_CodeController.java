package com.ankhang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Authen_CodeController {
    @Autowired
    private Environment environment;
    
	@GetMapping("/home")
	public String home() {
		System.out.println("--------- CALLING /HOMEAUTHEN PORT: ---------" + environment.getProperty("local.server.port"));
//      String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
//      String fullUrl = baseUrl + request.getRequestURI();
//      System.out.println("--------- CALLING " + fullUrl + " ---------");
		return "home";
	}
    

	
	@GetMapping("/homeauthen")
	public String homeauthen() {
		System.out.println("--------- CALLING /HOMEAUTHEN PORT: ---------" + environment.getProperty("local.server.port"));
		return "home";
	}
	
}