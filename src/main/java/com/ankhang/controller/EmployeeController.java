package com.ankhang.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ankhang.model.EmployeeModel;
import com.ankhang.service.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/employees/{id}")
	ResponseEntity<EmployeeModel> getEmployeeDetail(@PathVariable("id") Long id) {
		EmployeeModel employeeModel = employeeService.getEmployeeById(id);
		return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
	}

}
