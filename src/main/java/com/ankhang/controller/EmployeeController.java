package com.ankhang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.ankhang.model.EmployeeModel;
import com.ankhang.service.EmployeeService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	//clientcall with the same name in config datasource properties
	@GetMapping("/employees/{id}")
	@CircuitBreaker(name = "clientcall", fallbackMethod = "fallbackMethod")
	ResponseEntity<EmployeeModel> getEmployeeDetail(@PathVariable("id") Long id) {
		EmployeeModel employeeModel = employeeService.getEmployeeById(id);
		return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
	}
	
	
	ResponseEntity<EmployeeModel> fallbackMethod(@PathVariable("id") Long id, FeignException f) {
		System.out.println("CAN NOT CALL METHOD getEmployeeDetail -> will run this fallbackMethod");
    	EmployeeModel employeeModel = employeeService.getEmployeeById_NoCallServer(id);
		return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
	}
	

}
