package com.ankhang.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
@EnableAutoConfiguration(exclude = {   
		 HibernateJpaAutoConfiguration.class
      })
@EnableFeignClients(basePackages = "com.ankhang.feigncclient")
public class ProjectMicroserviceClientEmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectMicroserviceClientEmployeeApplication.class, args);
	}

}
