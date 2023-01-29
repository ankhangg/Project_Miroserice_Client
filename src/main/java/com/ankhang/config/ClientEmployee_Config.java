package com.ankhang.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientEmployee_Config {
	
	@Value("${info.url}")
	private String addressURL;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	//chỗ này rút gọn cho code đẹp hơn // case dung restTemplate => webClient
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public WebClient webClient() {
		return WebClient.builder().baseUrl(addressURL).build();
	}
}
