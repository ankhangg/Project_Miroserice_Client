package com.ankhang.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.constant.MethodConstant.CanCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.ankhang.model.EmployeeModel;
import com.ankhang.service.EmployeeService;
import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.persistence.criteria.CriteriaBuilder.Case;

@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	/* Case not use @TimeLimiter Start */
	//clientcall with the same name in config datasource properties
//	@GetMapping("/employees/{id}")
//	@CircuitBreaker(name = "clientcall", fallbackMethod = "fallbackMethod")
//	ResponseEntity<EmployeeModel> getEmployeeDetail(@PathVariable("id") Long id) {
//		EmployeeModel employeeModel = employeeService.getEmployeeById(id);
//		return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
//	}
//	
//	ResponseEntity<EmployeeModel> fallbackMethod(@PathVariable("id") Long id, FeignException f) {
//	System.out.println("CAN NOT CALL METHOD getEmployeeDetail -> will run this fallbackMethod");
//	EmployeeModel employeeModel = employeeService.getEmployeeById_NoCallServer(id);
//	return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
//     }
	
	/* Case not use @TimeLimiter END */
	
	@GetMapping("/employees/{id}")
	@CircuitBreaker(name = "clientcall", fallbackMethod = "fallbackMethod")
	@TimeLimiter(name = "clientcall")
	@Retry(name = "clientcall" , fallbackMethod = "fallbackMethodForRetry")
	public CompletableFuture<EmployeeModel> getEmployeeDetail(@PathVariable("id") Long id) {
	   
		/* IF USE LIKE THIS => WILL FAIL BECAUSE IT NOT IN 1 THREAD Start */
//		EmployeeModel employeeModel = employeeService.getEmployeeById(id);
//		return CompletableFuture.supplyAsync(() -> employeeModel);
		/* IF USE LIKE THIS => WILL FAIL BECAUSE IT NOT IN 1 THREAD End */
		
		/* CanCache use timeout for the CompletableFuture start */
		/*
		 * return CompletableFuture.supplyAsync(() -> { EmployeeModel employeeModel =
		 * employeeService.getEmployeeById(id); return employeeModel; }).orTimeout(3,
		 * TimeUnit.SECONDS); // set timeout for the CompletableFuture
		 */		
		/* CanCache use timeout for the CompletableFuture end */
		return CompletableFuture.supplyAsync(() -> {
	        EmployeeModel employeeModel = employeeService.getEmployeeById(id);
	        return employeeModel;
	    });
	}

	public CompletableFuture<EmployeeModel> fallbackMethod(@PathVariable("id") Long id, FeignException f) {
	    System.out.println("CAN NOT CALL METHOD getEmployeeDetail -> will run this fallbackMethod FeignException");
	    EmployeeModel employeeModel = employeeService.getEmployeeById_NoCallServer(id);
	    return CompletableFuture.supplyAsync(() -> employeeModel);
	}
	
	//Comment this because when use @Retry => at the first retry will go to this method => end retry => not correct
	// Just use in case use @CircuitBreaker @TimeLimiter
	/* This code to handle @TimeLimiter(name = "clientcall") START */
//	public CompletableFuture<EmployeeModel> fallbackMethod(@PathVariable("id") Long id, TimeoutException timeoutException) {
//	    System.out.println("CAN NOT CALL METHOD getEmployeeDetail -> will run this fallbackMethod TimeoutException");
//	    EmployeeModel employeeModel = employeeService.getEmployeeById_NoCallServer(id);
//	    return CompletableFuture.supplyAsync(() -> employeeModel);
//	}
	/* This code to handle @TimeLimiter(name = "clientcall") END */
	
	//This method handle when @Retry Fail
	public CompletableFuture<EmployeeModel> fallbackMethodForRetry(@PathVariable("id") Long id, TimeoutException timeoutException) {
	    System.out.println("CAN NOT CALL METHOD getEmployeeDetail -> will run this fallbackMethod TimeoutException");
	    EmployeeModel employeeModel = employeeService.getEmployeeById_NoCallServer(id);
	    return CompletableFuture.supplyAsync(() -> employeeModel);
	}
	

}
