package com.ankhang.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.persistence.criteria.CriteriaBuilder.Case;





@Configuration
@EnableWebSecurity
public class SecurityConfig {
	/* Case use authorization_code to authen web Start */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//
//        httpSecurity.authorizeHttpRequests()
//                .requestMatchers("/home").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login();
//
//       return  httpSecurity.build();
//    }
	/* Case use authorization_code to authen web End */
	
	/* Case use client_credentials to authen rest API Start */
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	    http
//	        .authorizeHttpRequests(authorize -> authorize
//	           .requestMatchers("/addurl_here").permitAll()
//	           .anyRequest().authenticated()
//	        )
//	        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//	    return http.build();
//	}
	
	/* Case use client_credentials to authen rest API End */
	
	
	/* Case use both client_credentials and authorization_code Start */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/home").permitAll()
	            .requestMatchers("/addurl_here").permitAll()
	            .anyRequest().authenticated()
	        )
	        .oauth2Login()
	        .and()
	        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

	    return http.build();
	}
	/* Case use both client_credentials and authorization_code End */
	

	/* use method instead of config in application.properties Start */
//  @Bean
//  public ClientRegistrationRepository clientRepository() {
//
//  	ClientRegistration keycloak = keycloakClientRegistration();
//  	return new InMemoryClientRegistrationRepository(keycloak);
//  }
//
//  private ClientRegistration keycloakClientRegistration() {
//
//  	return ClientRegistration.withRegistrationId("spring-boot-microservices-realm")
//  		.clientId("spring-cloud-client-test-account")
//  		.clientSecret("dCPCBA41LI6LxkQFfpQxK0PfP5LYxghl")
//  		.redirectUri("http://localhost:8080/login/oauth2/code/spring-cloud-client-test-account")
//  		.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//  		.issuerUri("http://localhost:8181/realms/spring-boot-microservices-realm")
//  		.authorizationUri("http://localhost:8181/realms/spring-boot-microservices-realm/protocol/openid-connect/auth")
//  		.tokenUri("http://localhost:8181/realms/spring-boot-microservices-realm/protocol/openid-connect/token")
//  		.userInfoUri("http://localhost:8181/realms/spring-boot-microservices-realm/protocol/openid-connect/userinfo")
//  		.build();
//  }
	/* use method instead of config in application.properties End */
    
}