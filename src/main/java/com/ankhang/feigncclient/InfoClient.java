package com.ankhang.feigncclient;

//import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ankhang.model.InfoModel;

//@FeignClient(name = "info-app", url = "http://localhost:8081/info-app")
@FeignClient(name = "info-app", path = "/info-app") // name = "info-app" mapping with server name
//@RibbonClient(name = "info-app")
public interface InfoClient {

	@GetMapping("/infos/{id}")
	public  InfoModel getInfoDetail(@PathVariable("id") Long id,  @RequestHeader("Authorization") String authorizationHeader);
	//@RequestHeader("Authorization") because server authen oAuth2 with Keyclock so need to implement
}
